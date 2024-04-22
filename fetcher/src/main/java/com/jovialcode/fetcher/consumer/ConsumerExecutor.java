package com.jovialcode.fetcher.consumer;

import com.jovialcode.fetcher.FetcherContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerExecutor {
    private final ExecutorService downloadExecutor;

    public ConsumerExecutor(FetcherContext context) {
        this.downloadExecutor = Executors.newFixedThreadPool(context.getProducerTask());
    }

    public void addConsumer(String host){
        Downloader downloader = new Downloader(host);
        this.downloadExecutor.execute(downloader);
    }
}
