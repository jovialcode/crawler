package com.jovialcode.manager.crawler;

import com.jovialcode.manager.web.crawler.CreateCrawler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlerRouterTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHello() {
        webTestClient
                // Create a GET request to test an endpoint
                .get().uri("/crawl")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // and use the dedicated DSL to test assertions against the response
                .expectStatus().isOk()
                .expectBody(CreateCrawler.class).value(crawl -> {
                    assertThat(crawl.getHost()).isEqualTo("Hello");
                });
    }
}
