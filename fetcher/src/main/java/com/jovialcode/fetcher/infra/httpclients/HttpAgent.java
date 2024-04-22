package com.jovialcode.fetcher.infra.httpclients;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Scope("prototype")
@Component
@RequiredArgsConstructor
public class HttpAgent {
    static private final String userAgent = "leona";
    static private final int connectionTimeout = 30000;
    static private final int connectionRequestTimeout = 30000;
    static private final int socketTimeout = 30000;

    private final HttpClient httpClient;

    public HttpAgent() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String download(String url) throws URISyntaxException {
        return download(new URI(url));
    }

    public String download(URI url) {
        HttpRequest httpRequest = HttpRequest.newBuilder(url)
                .GET()
                .build();

        return download(httpRequest);
    }

    public String download(HttpRequest httpRequest) {
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }
}
