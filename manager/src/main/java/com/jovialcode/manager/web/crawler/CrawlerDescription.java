package com.jovialcode.manager.web.crawler;

import com.jovialcode.manager.crawler.CrawlerStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CrawlerDescription {
    private Long id;
    private String name;
    private String description;
    private String host;
    private Integer concurrency;
    private CrawlerStatus status;
    private Timestamp create;
    private Timestamp modify;
    private Boolean delete;
}
