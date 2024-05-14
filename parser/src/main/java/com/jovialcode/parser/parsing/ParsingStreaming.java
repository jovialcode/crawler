package com.jovialcode.parser.parsing;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.mongodb.source.MongoSource;
import org.apache.flink.connector.mongodb.source.enumerator.splitter.PartitionStrategy;
import org.apache.flink.connector.mongodb.source.reader.deserializer.MongoDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.bson.BsonDocument;

import java.util.List;

public class ParsingStreaming {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        MongoSource<String> source = MongoSource.<String>builder()
            .setUri("mongodb://crawler:crawler123@mongodb:27017")
            .setDatabase("crawler")
            .setCollection("crawl_data")
            .setProjectedFields("_id", "crawlerId", "page")
            .setFetchSize(10)
            .setLimit(50)
            .setNoCursorTimeout(true)
            .setPartitionStrategy(PartitionStrategy.SAMPLE)
            .setPartitionSize(MemorySize.ofMebiBytes(64))
            .setSamplesPerPartition(2)
            .setDeserializationSchema(new MongoDeserializationSchema<>() {
                @Override
                public String deserialize(BsonDocument document) {
                    return document.toJson();
                }

                @Override
                public TypeInformation<String> getProducedType() {
                    return BasicTypeInfo.STRING_TYPE_INFO;
                }
            })
            .build();

        ParsingRule parsingRule = new ParsingRule("//*[@id=\"content\"]/div[5]/ul/li[1]/div[1]/div[2]/strong/span[1]/a", "name");
        Parsing htmlParser = new HtmlParser();
        String table = "temp";

        // MongoDBSource로부터 데이터를 읽어오는 DataStream 생성
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "crawl_data")
            .setParallelism(1)
            .map(new MapFunction<String, Tuple1<List<ParsingResult>>>() {
                @Override
                public Tuple1<List<ParsingResult>> map(String document) {
                    return new Tuple1<>(htmlParser.parse(document, List.of(parsingRule)));
                }
            })
            .flatMap(new FlatMapFunction<Tuple1<List<ParsingResult>>, ParsingResult>() {
                @Override
                public void flatMap(Tuple1<List<ParsingResult>> parsingResults, Collector<ParsingResult> out) throws Exception {
                    for (ParsingResult result : parsingResults.f0) {
                        out.collect(result);
                    }
                }
            })
            .map(new MapFunction<ParsingResult, ParsingResult>() {
                @Override
                public ParsingResult map(ParsingResult parsingResult) throws Exception {
                    System.out.println(parsingResult); // 파싱 결과 출력
                    return parsingResult;
                }
            })
            .addSink(
                JdbcSink.sink(
                    String.format("insert into %s (tag, value) values (?, ?)", table),
                    (statement, parseResult) -> {
                        statement.setString(1, parseResult.getTag());
                        statement.setString(2, parseResult.getValue());
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
