package com.jovialcode.fetcher.crawler;

public interface Crawler<T> extends Runnable{
    public T crawl(CrawlItem crawlItem);
}
