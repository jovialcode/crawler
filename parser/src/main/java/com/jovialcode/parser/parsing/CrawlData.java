package com.jovialcode.parser.parsing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class CrawlData implements Serializable {
    @JsonProperty("_id")
    private String url;
    @JsonProperty("_class")
    private String className;
    @JsonProperty("page")
    private String page;
    @JsonProperty("crawlerId")
    private Integer crawlerId;

    public CrawlData() {
    }

    public CrawlData(String url, String className, String page, Integer crawlerId) {
        this.url = url;
        this.className = className;
        this.page = page;
        this.crawlerId = crawlerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getCrawlerId() {
        return crawlerId;
    }

    public void setCrawlerId(Integer crawlerId) {
        this.crawlerId = crawlerId;
    }
}
