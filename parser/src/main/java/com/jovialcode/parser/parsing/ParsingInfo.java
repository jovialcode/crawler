package com.jovialcode.parser.parsing;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParsingInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("crawler_id")
    private Integer crawlerId;
    @JsonProperty("parsing_rules")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ParsingRule> parsingRules;

    public ParsingInfo() {
    }

    public ParsingInfo(Integer id, Integer crawlerId, List<ParsingRule> parsingRules) {
        this.id = id;
        this.crawlerId = crawlerId;
        this.parsingRules = parsingRules;
    }

    public List<ParsingResult> evaluate(Document page) {
        List<ParsingResult> result = new ArrayList<>();

        for(ParsingRule rule: parsingRules){
            List<ParsingResult> evaluate = rule.evaluate(page);
            result.addAll(evaluate);
        }

        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCrawlerId() {
        return crawlerId;
    }

    public void setCrawlerId(Integer crawlerId) {
        this.crawlerId = crawlerId;
    }

    public List<ParsingRule> getParsingRules() {
        return parsingRules;
    }

    public void setParsingRules(List<ParsingRule> parsingRules) {
        this.parsingRules = parsingRules;
    }
}
