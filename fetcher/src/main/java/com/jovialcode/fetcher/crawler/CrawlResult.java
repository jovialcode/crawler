package com.jovialcode.fetcher.crawler;

import lombok.Data;

import java.net.URI;

@Data(staticConstructor = "of")
public class CrawlResult {
    private final URI uri;
    private final String response;
}
