package com.jovialcode.common.crawler;

import lombok.Data;

import java.net.URI;

@Data(staticConstructor = "of")
public class CrawlItem {
    private final URI uri;
    private int retry = 0;
}