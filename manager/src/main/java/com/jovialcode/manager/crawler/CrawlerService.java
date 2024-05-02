package com.jovialcode.manager.crawler;

import com.jovialcode.manager.web.crawler.CrawlerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final CrawlerRepository crawlerRepository;
    private final CrawlerMapper crawlerMapper;

    public Flux<Crawler> crawlers(){
        return crawlerRepository.findAll();
    }
}
