package com.jovialcode.manager.crawler;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("crawler")
@Getter
@NoArgsConstructor
public class Crawler {
    @Id
    private Long id;
    private String name;
    private String description;

    private String host;
    private Integer concurrency;
    private CrawlerStatus status;

    @CreatedDate
    private Timestamp create;
    @LastModifiedDate
    private Timestamp modify;

    private Boolean delete;
}
