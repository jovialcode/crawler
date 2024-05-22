package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParsingItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private CrawlData crawlData;
    private ParsingInfo parsingInfo;

    public ParsingItem() {
    }

    public ParsingItem(CrawlData crawlData, ParsingInfo parsingInfo) {
        this.crawlData = crawlData;
        this.parsingInfo = parsingInfo;
    }

    public CrawlData getCrawlData() {
        return crawlData;
    }

    public void setCrawlData(CrawlData crawlData) {
        this.crawlData = crawlData;
    }

    public ParsingInfo getParsingInfo() {
        return parsingInfo;
    }

    public void setParsingInfo(ParsingInfo parsingInfo) {
        this.parsingInfo = parsingInfo;
    }
}
