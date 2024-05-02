package com.jovialcode.manager.web.crawler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jovialcode.manager.crawler.Crawler;


@Component
@RequiredArgsConstructor
public class CrawlerMapper {
    private final ObjectMapper objectMapper;

    public CrawlerDescription mapToDTO(Crawler crawler) {
        return objectMapper.convertValue(crawler, CrawlerDescription.class);
    }
}
