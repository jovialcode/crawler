package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovialcode.parser.common.FlinkDocument;
import com.jovialcode.parser.common.FlinkRow;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParsingProcess extends BroadcastProcessFunction<String, String, ParsingItem> {
    private static final Logger logger = LoggerFactory.getLogger(ParsingProcess.class);

    private final String PARSING_INFO = "parsing_info";

    private static final MapStateDescriptor<String, ParsingInfo> broadcastStateDescriptor =
        new MapStateDescriptor<>("broadcast-state", String.class, ParsingInfo.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

    @Override
    public void processElement(String document, ReadOnlyContext ctx, Collector<ParsingItem> out) throws Exception {
        ReadOnlyBroadcastState<String, ParsingInfo> broadcastState = ctx.getBroadcastState(broadcastStateDescriptor);
        FlinkDocument<CrawlData> flinkDocument = objectMapper.readValue(document, new TypeReference<>() {});
        CrawlData crawlData = flinkDocument.getFullDocument();
        ParsingItem parsingItem = new ParsingItem(
            crawlData,
            broadcastState.get(PARSING_INFO)
        );

        logger.info("ParsingProcess: parsingItem: {}", parsingItem);
        out.collect(parsingItem);
    }

    @Override
    public void processBroadcastElement(String row,
                                        BroadcastProcessFunction<String, String, ParsingItem>.Context ctx,
                                        Collector<ParsingItem> out) throws Exception {
        FlinkRow<ParsingInfo> flinkRow = objectMapper.readValue(row, new TypeReference<>() {});
        ParsingInfo parsingInfo = flinkRow.getAfter();
        logger.info("ParsingProcess: parsingInfo: {}", parsingInfo);
        BroadcastState<String, ParsingInfo> broadcastState = ctx.getBroadcastState(broadcastStateDescriptor);
        broadcastState.put(PARSING_INFO, parsingInfo);
    }


}
