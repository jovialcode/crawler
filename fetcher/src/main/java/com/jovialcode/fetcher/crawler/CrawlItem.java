package com.jovialcode.fetcher.crawler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrawlItem {
    private String url;
    private
    private int retry = 0;
}