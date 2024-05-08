package com.jovialcode.common.urls;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;


class CleanedUrlTest {
    @Test
    void testReversedUrl() throws URISyntaxException {
        String url = "https://chatgpt.com/c/12eddfa5-4461-4de9-8b12-6214deed8dba";
        CleanedUrl cleanedUrl = new CleanedUrl(url);
        String result = cleanedUrl.getValue();
    }

    @Test
    void testReversedUri() throws URISyntaxException {
        String url = "https://chatgpt.com/c/12eddfa5-4461-4de9-8b12-6214deed8dba";
        URI uri = new URI(url);
        CleanedUrl cleanedUrl = new CleanedUrl(uri);
        String result = cleanedUrl.getValue();
    }

    @Test
    void testReversedUrlWithQueryString() throws URISyntaxException {
        String url = "http://www.example.com/page?query=example";
        CleanedUrl cleanedUrl = new CleanedUrl(url);
        String result = cleanedUrl.getValue();
    }
}