package com.jovialcode.common.urls;

import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
public class CleanedUrl {
    private final String origin;
    private final String value;

    public CleanedUrl(String url) throws URISyntaxException {
        this.origin = url;
        this.value = removeProtocolAndQueryString(url);
    }

    public CleanedUrl(URI uri) {
        this.origin = uri.toString();
        this.value = removeProtocolAndQueryString(uri);
    }

    private String removeProtocolAndQueryString(String url) throws URISyntaxException {
        return removeProtocolAndQueryString(new URI(url));
    }

    private String removeProtocolAndQueryString(URI uri) {
        String path = uri.getPath();
        String host = uri.getHost();
        return (host != null ? host : "") + (path != null ? path : "");
    }
}
