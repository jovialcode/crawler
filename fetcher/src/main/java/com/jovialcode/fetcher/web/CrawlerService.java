package com.jovialcode.fetcher.web;

import com.jovialcode.fetcher.crawler.CrawlerContext;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {
    public void createCrawler(){
        final int concurrency = 2;
        CrawlerContext crawlerContext = new CrawlerContext(concurrency);
    }
}
