package com.jovialcode.parser.parsing;

import com.ververica.cdc.connectors.base.options.StartupOptions;
import com.ververica.cdc.connectors.mongodb.source.MongoDBSource;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class ParsingStreaming {
    private static final Logger logger = LoggerFactory.getLogger(ParsingStreaming.class);

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MongoDBSource<String> documentSource = MongoDBSource.<String>builder()
            .hosts("mongo1:27017,mongo2:27017,mongo3:27017/?replicaSet=rs0")
            .scheme("mongodb")
            .username("crawler")
            .password("crawler123")
            .databaseList("crawler") // set captured database, support regex
            .collectionList("crawler.crawl_data")
            .startupOptions(StartupOptions.latest())
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();

        MySqlSource<String> parsingRuleSource = MySqlSource.<String>builder()
            .hostname("mysql")
            .port(3306)
            .databaseList("crawler")
            .username("root")
            .password("admin!23")
            .tableList("crawler.parsing_rule")
            .serverTimeZone("Asia/Seoul")
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();


        MapStateDescriptor<String, ParsingInfo> broadcastStateDescriptor =
            new MapStateDescriptor<>("broadcast-state", String.class, ParsingInfo.class);

        BroadcastStream<String> parsingRuleStream = env.fromSource(
            parsingRuleSource,
            WatermarkStrategy.forMonotonousTimestamps(),
            "parsingRule Source")
            .broadcast(broadcastStateDescriptor);

        HtmlParser htmlParser = new HtmlParser();

        // MongoDBSource로부터 데이터를 읽어오는 DataStream 생성
        DataStreamSource<String> crawlDataStream = env.fromSource(documentSource, WatermarkStrategy.forMonotonousTimestamps(), "crawl_data");
        crawlDataStream
            .setParallelism(2)
            .connect(parsingRuleStream)
            .process(new ParsingProcess())
            .map(new MapFunction<ParsingItem, Tuple1<List<ParsingResult>>>() {
                @Override
                public Tuple1<List<ParsingResult>> map(ParsingItem parsingItem) {
                    return new Tuple1<>(htmlParser.parse(parsingItem.getCrawlData(), parsingItem.getParsingInfo()));
                }
            })
            .flatMap(new FlatMapFunction<Tuple1<List<ParsingResult>>, Tuple1<ParsingResult>>() {
                @Override
                public void flatMap(Tuple1<List<ParsingResult>> parsingResults, Collector<Tuple1<ParsingResult>> out) throws Exception {
                    if(!Objects.isNull(parsingResults.f0)){
                        for (ParsingResult result : parsingResults.f0) {
                            out.collect(new Tuple1<>(result));
                        }
                    }
                }
            })
            .addSink(
                JdbcSink.sink(
                    String.format("insert into %s (tag, value) values (?, ?)", "temp"),
                    (statement, parseResult) -> {
                        statement.setString(1, parseResult.f0.getTag());
                        statement.setString(2, parseResult.f0.getValue());
                    },
                    JdbcExecutionOptions.builder()
                        .withBatchSize(1)
                        .withBatchIntervalMs(200)
                        .withMaxRetries(5)
                        .build(),
                    new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://mysql:3306/crawl_data")
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUsername("root")
                        .withPassword("admin!23")
                        .build()
                )
            );
        // Job 실행
        env.execute("Parsing Streaming");
    }
}
