package com.jovialcode.manager.crawler;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends ReactiveCrudRepository<Crawler, Long> {
}
