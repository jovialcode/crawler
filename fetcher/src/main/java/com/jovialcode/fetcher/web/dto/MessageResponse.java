package com.jovialcode.fetcher.web.dto;

import lombok.Data;

@Data(staticConstructor = "of")
public class MessageResponse {
    private final String message;
}
