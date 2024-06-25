package com.jovialcode.manager.domains.crawler;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRepository extends R2dbcRepository<Crawler, Long> {
}
