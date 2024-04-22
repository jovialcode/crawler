package com.jovialcode.fetcher;

import com.jovialcode.fetcher.consumer.ConsumerExecutor;

public class Fetcher {
    private final FetcherContext context;

    private ConsumerExecutor consumerExecutor;

    public Fetcher(FetcherContext context) {
        this.context = context;
    }

    public void init(){
        this.consumerExecutor = new ConsumerExecutor(context);
        this.consumerExecutor.addConsumer("www.koreabaseball.com");
    }
}
