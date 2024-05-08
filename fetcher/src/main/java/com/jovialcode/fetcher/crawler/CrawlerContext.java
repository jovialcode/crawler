package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import com.jovialcode.fetcher.infra.httpclients.HttpAgent;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerContext {
    private final String crawlerName;
    private final int concurrency;

    private final Crawler crawler;
    private final ExecutorService executorService;

    public CrawlerContext(String crawlerName, int concurrency, Consumer<String, CrawlItem> consumer) {
        this.crawlerName = crawlerName;
        this.concurrency = concurrency;

        this.executorService = Executors.newFixedThreadPool(concurrency);

        HttpAgent httpAgent = new HttpAgent();
        consumer.subscribe(Collections.singletonList(crawlerName));
        this.crawler = new WebCrawler(httpAgent, consumer);
    }

    public boolean start(){
        this.executorService.submit(crawler);
        return true;
    }

    public boolean stop(){
        this.executorService.shutdown();
        return true;
    }
}
