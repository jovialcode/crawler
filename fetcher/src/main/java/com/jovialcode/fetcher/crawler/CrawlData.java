package com.jovialcode.fetcher.crawler;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Data(staticConstructor = "of")
@Document(collection = "crawl_data")
public class CrawlData{
    @Id
    private final String url;
    private final String page;

    private final Integer crawlerId;

    @Override
    public String toString() {
        return "CrawlData{" +
            "url='" + url + '\'' +
            ", crawlerId=" + crawlerId +
            '}';
    }
}
