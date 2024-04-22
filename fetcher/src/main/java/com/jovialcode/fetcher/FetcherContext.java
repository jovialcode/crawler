package com.jovialcode.fetcher;


import lombok.Getter;

public class FetcherContext {
    @Getter private final int producerTask = 1;
    @Getter private final int consumerTask = 2;

    @Getter private static final String TOPIC_NAME = "schedule";
    @Getter private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    @Getter private static final String GROUP_ID = "my-group";
}
