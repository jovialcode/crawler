package com.jovialcode.parser.parsing;


import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ParsingRule implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String xPath;
    private final String tag;

    public ParsingRule(String xPath, String tag){
        this.xPath = xPath;
        this.tag = tag;
    }

    public List<ParsingResult> evaluate(Document page) {
        Elements nodes = page.selectXpath(this.xPath);

        return nodes
            .stream()
            .map(textNode -> new ParsingResult(tag, textNode.text()))
            .collect(Collectors.toList());
    }
}
