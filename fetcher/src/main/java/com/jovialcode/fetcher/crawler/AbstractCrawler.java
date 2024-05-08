package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlerState;

public abstract class AbstractCrawler implements Crawler {
    private CrawlerState state = CrawlerState.INIT;
}

