package com.jovialcode.fetcher.infra.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        this.httpClient = HttpClient.newBuilder().build();
    }

    public HttpResponse<String> download(String url) throws URISyntaxException, IOException, InterruptedException {
        return download(new URI(url));
    }

    public HttpResponse<String> download(URI url) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder(url)
                .GET()
                .build();

        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
