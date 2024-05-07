package com.jovialcode.fetcher.crawler;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/***
* 만약 여기서
 * Crawl Delay를 어떻게 적용하는게 좋을까?
 *
* **/

public class WebCrawler extends AbstractCrawler{
    private final HttpClient httpClient;
    private final Consumer<String, CrawlItem> consumer;

    public WebCrawler(Consumer<String, CrawlItem> consumer) {
        this.httpClient = HttpClient
            .newBuilder()
            .build(); // 이건 Bean으로 분리.
        this.consumer = consumer;
    }

    @Override
    public CrawlResult crawl(CrawlItem crawlItem) {

    }

    @Override
    public void run() {
        try{
            while (true) {
                ConsumerRecords<String, CrawlItem> records = this.consumer.poll(Duration.ofMillis(3000));
                for (ConsumerRecord<String, CrawlItem> record : records) {

                    CrawlResult result = crawl(record.value());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
