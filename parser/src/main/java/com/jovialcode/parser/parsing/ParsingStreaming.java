package com.jovialcode.parser.parsing;

import com.ververica.cdc.connectors.base.options.StartupOptions;
import com.ververica.cdc.connectors.mongodb.source.MongoDBSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Objects;

public class ParsingStreaming {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MongoDBSource<String> source = MongoDBSource.<String>builder()
            .hosts("mongo1:27017,mongo2:27017,mongo3:27017/?replicaSet=rs0")
            .scheme("mongodb")
            .username("crawler")
            .password("crawler123")
            .databaseList("crawler") // set captured database, support regex
            .collectionList("crawler.crawl_data")
            .startupOptions(StartupOptions.latest())
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();

        ParsingRule parsingRule = new ParsingRule("//*[@id=\"content\"]/div[5]/ul/li[1]/div[1]/div[2]/strong/span[1]/a", "name");
        Parsing htmlParser = new HtmlParser();
        String table = "temp";

        // MongoDBSource로부터 데이터를 읽어오는 DataStream 생성
        env.fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "crawl_data")
            .setParallelism(1)
            .map(new MapFunction<String, Tuple1<List<ParsingResult>>>() {
                @Override
                public Tuple1<List<ParsingResult>> map(String document) {
                    return new Tuple1<>(htmlParser.parse(document, List.of(parsingRule)));
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
                    String.format("insert into %s (tag, value) values (?, ?)", table),
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
        env.execute("final mongoDB -> Mysql");
    }
}
