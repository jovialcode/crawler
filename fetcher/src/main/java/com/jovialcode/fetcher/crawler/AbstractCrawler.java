package com.jovialcode.fetcher.crawler;

public abstract class AbstractCrawler implements Crawler {
    private CrawlerState state = CrawlerState.INIT;
    private final int concurrency = 1;
}

