package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import com.jovialcode.fetcher.infra.httpclients.HttpAgent;
import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
public class CrawlerContext {
    private final Integer crawlerId;
    private final String crawlerName;
    private final Integer concurrency;

    private final Crawler<CrawlResult> crawler;
    private final ExecutorService executorService;

    public CrawlerContext(Integer crawlerId,
                          String crawlerName,
                          Integer concurrency,
                          MongoRepository<CrawlData, String> crawlDataRepository,
                          Consumer<String, CrawlItem> consumer) {
        this.crawlerId = crawlerId;
        this.crawlerName = crawlerName;
        this.concurrency = concurrency;

        this.executorService = Executors.newFixedThreadPool(concurrency);

        HttpAgent httpAgent = new HttpAgent();
        consumer.subscribe(Collections.singletonList(crawlerName));
        this.crawler = new WebCrawler(httpAgent, consumer, crawlDataRepository);
    }

    public boolean start() {

        Future<?> future = this.executorService.submit(this.crawler);
        try{
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean stop() {
        this.executorService.shutdown();
        return true;
    }
}
