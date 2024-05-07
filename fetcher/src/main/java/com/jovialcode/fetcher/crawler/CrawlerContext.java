package com.jovialcode.fetcher.crawler;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerContext {
    private final Properties props;
    private final Consumer<String, CrawlItem> consumer;

    private final Crawler crawler;
    private final ExecutorService executorService;

    public CrawlerContext(int concurrency) {
        this.executorService = Executors.newFixedThreadPool(concurrency);

        this.props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "123");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonObjectDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
        this.crawler = new WebCrawler(consumer);
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
