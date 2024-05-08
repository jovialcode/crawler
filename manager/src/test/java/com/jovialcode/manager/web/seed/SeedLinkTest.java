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
        URI uri = new URI("https://www.snuh.org/reservation/meddept/PDID/childDoctor.do");
        CrawlItem crawlItem = new CrawlItem(uri);
        seedLink.send("healty", crawlItem);
    }
}