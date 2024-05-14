package com.jovialcode.parser.infra;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class CrawlDataJDBCSink {
    public static SinkFunction of(String table){
        return JdbcSink.sink(
            String.format("insert into %s (tag, value) values (?, ?)", table),
            (statement, parseResult) -> {
                statement.setString(1, "parseResult.tag"); //type 추론이 필요함.
                statement.setString(2, "parseResult.value");
            },
            JdbcExecutionOptions.builder()
                .withBatchSize(1000)
                .withBatchIntervalMs(200)
                .withMaxRetries(5)
                .build(),
            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://mysql:3306/crawl_data")
                .withDriverName("org.postgresql.Driver")
                .withUsername("admin")
                .withPassword("admin!23")
                .build()
        );
    }
}
