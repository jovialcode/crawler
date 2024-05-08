package com.jovialcode.fetcher.crawler;

import com.jovialcode.common.crawler.CrawlItem;
import com.jovialcode.common.crawler.CrawlResult;
import com.jovialcode.fetcher.infra.httpclients.HttpAgent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

/***
* 만약 여기서
 * Crawl Delay를 어떻게 적용하는게 좋을까?
 *
* **/

public class WebCrawler extends AbstractCrawler{
    private final HttpAgent httpAgent;
    private final Consumer<String, CrawlItem> consumer;

    public WebCrawler(final HttpAgent httpAgent, final Consumer<String, CrawlItem> consumer) {
        this.httpAgent = httpAgent;
        this.consumer = consumer;
    }

    @Override
    public CrawlResult crawl(CrawlItem crawlItem) {
        String download = this.httpAgent.download(crawlItem.getUri());
        return CrawlResult.of(crawlItem.getUri(), download);
    }

    @Override
    public void run() {
        try{
            while (true) {
                ConsumerRecords<String, CrawlItem> records = this.consumer.poll(Duration.ofMillis(3000));
                for (ConsumerRecord<String, CrawlItem> record : records) {

                    CrawlResult result = crawl(record.value());
                    System.out.println(result.getResponse());
                }
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
