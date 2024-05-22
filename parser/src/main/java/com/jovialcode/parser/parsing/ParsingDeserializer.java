package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovialcode.parser.common.FlinkDocument;
import com.jovialcode.parser.common.FlinkRow;

public class ParsingDeserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public ParsingItem deserialize(String document, String row){
        ParsingItem parsingItem = new ParsingItem();

        try{
            FlinkDocument<CrawlData> flinkDocument = objectMapper.readValue(document, new TypeReference<>() {});
            CrawlData crawlData = flinkDocument.getFullDocument();
            parsingItem.setCrawlData(crawlData);
            //
            FlinkRow<ParsingInfo> flinkRow = objectMapper.readValue(row, new TypeReference<>() {});
            ParsingInfo parsingInfo = flinkRow.getAfter();
            parsingItem.setParsingInfo(parsingInfo);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return parsingItem;
    }
}
