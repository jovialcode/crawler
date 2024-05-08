package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;

public interface Crawler<T> extends Runnable{
    public T crawl(CrawlItem crawlItem);
}
