package com.jovialcode.fetcher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FetcherApplicationTests {
    @Autowired
    private Fetcher fetcher;

    @Test
    void downloadTest() {
        String url = "https://www.koreabaseball.com/Record/Player/HitterBasic/Basic1.aspx";
        fetcher.fetch(url);
    }
}
