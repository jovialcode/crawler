package com.jovialcode.manager.web.crawler;

import lombok.Getter;

import java.util.List;

public class CreateCrawler {
    @Getter
    private String host;
    @Getter
    private List<String> urls;

    public CreateCrawler(String host) {
        this.host = host;
    }


}
