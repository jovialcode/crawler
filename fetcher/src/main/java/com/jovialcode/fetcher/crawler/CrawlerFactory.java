package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlerFactory {
    private final ConsumerFactory<String, CrawlItem> consumerFactory;
    private final MongoRepository<CrawlData, String> crawlDataRepository;

    public CrawlerContext createCrawler(String crawlerName, int concurrency){
        Consumer<String, CrawlItem> consumer = consumerFactory.createConsumer(Integer.toString(concurrency));
        return new CrawlerContext(1, crawlerName, concurrency, crawlDataRepository, consumer);
    }
}
