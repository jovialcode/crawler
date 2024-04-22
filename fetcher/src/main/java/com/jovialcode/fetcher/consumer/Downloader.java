package com.jovialcode.fetcher.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class Downloader implements Runnable{
    private final String rootUrl; // partition hash value
    private final int crawlDelay = 300;
    private final Properties props;
    private String state = "";

    private final HttpClient httpClient;
    private final Consumer<String, String> consumer;

    public Downloader(String rootUrl) {
        this.rootUrl = rootUrl;
        this.state = "hey";
        this.httpClient = HttpClient
                .newBuilder()
                .build();

        this.props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "123");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(this.rootUrl));
    }

    @Override
    public void run() {
        try{
            while (true) {
                ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(crawlDelay));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("[%s] Received message: key = %s, value = %s, partition = %d, offset = %d%n",
                            Thread.currentThread().getName(), record.key(), record.value(), record.partition(), record.offset());

                    DownloadResponse download = download(record.value());
                }
                Thread.sleep(crawlDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private DownloadResponse download(String url) {
        DownloadResponse.DownloadResponseBuilder builder = DownloadResponse.builder();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();
        CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        httpResponseCompletableFuture.thenApply(response ->
            builder.collectStatus(DownloadResponse.DownloadStatus.SUCCESS)
                    .contents(response.body())
        ).join();

        return builder.build();
    }
}

