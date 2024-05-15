package com.jovialcode.parser.flink;

import com.jovialcode.parser.common.MongoJsonDeserializationSchema;
import com.jovialcode.parser.parsing.HtmlParser;
import com.jovialcode.parser.parsing.Parsing;
import com.jovialcode.parser.parsing.ParsingResult;
import com.jovialcode.parser.parsing.ParsingRule;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.mongodb.source.MongoSource;
import org.apache.flink.connector.mongodb.source.enumerator.splitter.PartitionStrategy;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.apache.flink.util.Collector;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoAsSourceTest {
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
        MongoSource<String> source = MongoSource.<String>builder()
            .setUri("mongodb://crawler:crawler123@localhost:27017")
            .setDatabase("crawler")
            .setCollection("crawl_data")
            .setProjectedFields("_id", "_class", "crawlerId", "page")
            .setFetchSize(10)
            .setLimit(50)
            .setNoCursorTimeout(true)
            .setPartitionStrategy(PartitionStrategy.SAMPLE)
            .setPartitionSize(MemorySize.ofMebiBytes(100))
            .setSamplesPerPartition(1)
            .setDeserializationSchema(new MongoJsonDeserializationSchema())
            .build();
        //
        ParsingRule parsingRule = new ParsingRule("//*[@id=\"content\"]/div[5]/ul/li[1]/div[1]/div[2]/strong/span[1]/a", "name");
        Parsing htmlParser = new HtmlParser();

        //
        CollectSink.values.clear();
        env.fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "crawl_data")
            .setParallelism(1)
            .map(new MapFunction<String, Tuple1<List<ParsingResult>>>() {
                @Override
                public Tuple1<List<ParsingResult>> map(String document) {
                    return new Tuple1(htmlParser.parse(document, List.of(parsingRule)));
                }
            })
            .flatMap(new FlatMapFunction<Tuple1<List<ParsingResult>>, Tuple1<ParsingResult>>() {
                @Override
                public void flatMap(Tuple1<List<ParsingResult>> parsingResults, Collector<Tuple1<ParsingResult>> out) throws Exception {
                    for (ParsingResult result : parsingResults.f0) {
                        out.collect(new Tuple1<>(result));
                    }
                }
            })
            .addSink(new CollectSink());

        // execute
        env.execute();
        Assertions.assertFalse(CollectSink.values.isEmpty());
    }

    private static class CollectSink implements SinkFunction<Tuple1<ParsingResult>> {

        public static final List<Tuple1<ParsingResult>> values = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void invoke(Tuple1<ParsingResult> value, SinkFunction.Context context) {
            values.add(value);
        }
    }

}
