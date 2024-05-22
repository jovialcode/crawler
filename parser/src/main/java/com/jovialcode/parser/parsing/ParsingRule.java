package com.jovialcode.parser.parsing;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParsingRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("pattern")
    private String pattern;
    @JsonProperty("tag")
    private String tag;

    public ParsingRule() {
    }

    public ParsingRule(String pattern, String tag) {
        this.pattern = pattern;
        this.tag = tag;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<ParsingResult> evaluate(Document page) {
        Elements nodes = page.selectXpath(this.pattern);

        if(Objects.isNull(nodes)) return new ArrayList<>();

        return nodes
            .stream()
            .map(textNode -> new ParsingResult(tag, textNode.text()))
            .collect(Collectors.toList());
    }
}
