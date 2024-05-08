package com.jovialcode.manager.web.seed;

import com.jovialcode.common.crawler.CrawlItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest
class SeedLinkTest {

    @Autowired
    private SeedLink seedLink;

    @Test
    void send() throws URISyntaxException {
        URI uri = new URI("https://www.gnuch.co.kr/gnuh/treat/docList.do?rbsIdx=55&it=2&code=IMG");
        CrawlItem crawlItem = CrawlItem.of(uri);
        seedLink.send("healty", crawlItem);
    }
}