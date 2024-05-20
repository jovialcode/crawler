package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jovialcode.parser.common.FlinkData;
import com.jovialcode.parser.common.FlinkDocumentDeserializer;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlParser extends AbstractParser {
    private static final long serialVersionUID = 2L;

    private final ObjectMapper objectMapper;

    public HtmlParser() {
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<ParsingResult> parse(String page, List<ParsingRule> parsingRules) {
        try {
            FlinkData<CrawlData> flinkData = objectMapper.readValue(page, new TypeReference<>() {});
            CrawlData crawlData = flinkData.getFullDocument();
            Document document = Jsoup.parse(crawlData.getPage());

            List<ParsingResult> collect = parsingRules
                .stream()
                .map(parsingRule -> parsingRule.evaluate(document))
                .flatMap(List::stream)
                .collect(Collectors.toList());
            return collect;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }
}
