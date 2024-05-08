package com.jovialcode.common.crawler;

import lombok.*;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawlItem {
    private URI uri;
    private int retry = 0;

    public CrawlItem(URI uri) {
        this.uri = uri;
    }
}