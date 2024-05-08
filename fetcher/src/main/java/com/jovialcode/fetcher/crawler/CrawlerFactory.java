package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerFactory {
    private final ConsumerFactory<String, CrawlItem> consumerFactory;

    public CrawlerContext createCrawler(String crawlerName, int concurrency){
        Consumer<String, CrawlItem> consumer = consumerFactory.createConsumer();
        return new CrawlerContext(crawlerName, concurrency, consumer);
    }
}
