package com.jovialcode.parser.flink;

import com.jovialcode.parser.parsing.*;
import com.ververica.cdc.connectors.base.options.StartupOptions;
import com.ververica.cdc.connectors.mongodb.source.MongoDBSource;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoCDCMultiSourcesTest {
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
        MongoDBSource<String> documentSource = MongoDBSource.<String>builder()
            .hosts("localhost:27017")
            .scheme("mongodb")
            .username("crawler")
            .password("crawler123")
            .databaseList("crawler") // set captured database, support regex
            .collectionList("crawler.crawl_data")
            .startupOptions(StartupOptions.latest())
            .deserializer(new JsonDebeziumDeserializationSchema())
            .build();

        MySqlSource<String> parsingRuleSource = MySqlSource.<String>builder()
            .hostname("localhost")
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
            .map(new MapFunction<Tuple2<String, String>, ParsingItem>() {
                @Override
                public ParsingItem map(Tuple2<String, String> value) throws Exception {
                    return new ParsingDeserializer().deserialize(value.f0, value.f1);
                }
            }).addSink(new CollectSink());

        env.execute();
        Assertions.assertFalse(CollectSink.values.isEmpty());
    }

    private static class CollectSink implements SinkFunction<ParsingItem> {

        public static final List<ParsingItem> values = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void invoke(ParsingItem value, Context context) {
            values.add(value);
        }
    }

}
