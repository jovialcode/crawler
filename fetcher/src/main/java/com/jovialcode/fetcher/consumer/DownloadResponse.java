package com.jovialcode.fetcher.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.http.HttpResponse;

@Getter
@Builder
public class DownloadResponse {
    private String contents;
    private DownloadStatus collectStatus;
    private HttpResponse<String> httpResponse;

    @AllArgsConstructor
    public enum DownloadStatus{
        SUCCESS("SUCCESS", ""),
        FAIL("FAIL", ""),
        CRAWL_DENY("CRAWL_DENY", "");

        private final String status;
        private final String description;

        public String getStatus() {
            return status;
        }

        public String getDescription() {
            return description;
        }
    }
}