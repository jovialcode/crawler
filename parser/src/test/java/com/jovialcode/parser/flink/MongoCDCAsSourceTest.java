package com.jovialcode.parser.flink;

import com.jovialcode.parser.parsing.HtmlParser;
import com.jovialcode.parser.parsing.Parsing;
import com.jovialcode.parser.parsing.ParsingResult;
import com.jovialcode.parser.parsing.ParsingRule;
import com.ververica.cdc.connectors.mongodb.source.MongoDBSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoCDCAsSourceTest {
    @ClassRule
    public static MiniClusterWithClientResource miniClusterWithClientResource =
        new MiniClusterWithClientResource(
            new MiniClusterResourceConfiguration.Builder()
                .setNumberSlotsPerTaskManager(2)
                .setNumberTaskManagers(1)
                .build());
    @Test
    public void testMongoDBSource() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //
        MongoDBSource<String> source = MongoDBSource.<String>builder()
            .hosts("localhost:27017")
            .scheme("mongodb")
            .username("crawler")
            .password("crawler123")
            .databaseList("crawler")
            .collectionList("crawler.crawl_data")
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();
        //
        ParsingRule parsingRule = new ParsingRule("//*[@id=\"content\"]/div[5]/ul/li[1]/div[1]/div[2]/strong/span[1]/a", "name");
        Parsing htmlParser = new HtmlParser();
        String table = "temp";

        CollectSink.values.clear();
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "crawl_data")
            .addSink(new CollectSink());

        env.execute();
        Assertions.assertFalse(CollectSink.values.isEmpty());
    }

    private static class CollectSink implements SinkFunction<String> {

        public static final List<String> values = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void invoke(String value, Context context) {
            values.add(value);
        }
    }

}
