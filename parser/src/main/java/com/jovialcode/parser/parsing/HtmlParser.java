package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovialcode.parser.common.FlinkDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlParser extends AbstractParser {
    private static final long serialVersionUID = 2L;
    private static final Logger logger = LoggerFactory.getLogger(ParsingStreaming.class);

    private final ObjectMapper objectMapper;

    public HtmlParser() {
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<ParsingResult> parse(CrawlData crawlData, ParsingInfo parsingInfo) {
        try {
            Document document = Jsoup.parse(crawlData.getPage());

            logger.info("Html Parsing Url - {} ", crawlData.getUrl());
            logger.info("Html Parsing pattern - {} ", parsingInfo.getParsingRules());

            List<ParsingResult> collect = parsingInfo.getParsingRules()
                .stream()
                .map(parsingRule -> parsingRule.evaluate(document))
                .flatMap(List::stream)
                .collect(Collectors.toList());

            logger.info("Html Parsing collect - {} ", collect);
            return collect;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
