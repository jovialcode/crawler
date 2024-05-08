package com.jovialcode.fetcher.crawler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrawlerFactoryTest {
    @Autowired
    private CrawlerFactory crawlerFactory;

    @Test
    void createCrawler() throws InterruptedException {
        CrawlerContext healty = crawlerFactory.createCrawler("healty", 1);
        healty.start();
    }
}