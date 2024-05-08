package com.jovialcode.fetcher.crawler;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlDataRepository extends MongoRepository<CrawlData, String> {
}
