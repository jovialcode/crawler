package com.jovialcode.parser.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlParser extends AbstractParser {
    private static final long serialVersionUID = 2L;

    @Override
    public List<ParsingResult> parse(String page, List<ParsingRule> parsingRules){
        Document document = Jsoup.parse(page);

        return parsingRules
            .stream()
            .map(parsingRule -> parsingRule.evaluate(document))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
