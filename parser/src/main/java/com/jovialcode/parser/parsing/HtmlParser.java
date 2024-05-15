package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlParser extends AbstractParser {
    private static final long serialVersionUID = 2L;
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public List<ParsingResult> parse(String page, List<ParsingRule> parsingRules){
        try{
            CrawlData crawlData = objectMapper.readValue(page, CrawlData.class);
            Document document = Jsoup.parse(crawlData.getPage());

            return parsingRules
                .stream()
                .map(parsingRule -> parsingRule.evaluate(document))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
