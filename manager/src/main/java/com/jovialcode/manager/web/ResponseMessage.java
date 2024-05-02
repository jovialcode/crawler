package com.jovialcode.manager.web;

import lombok.Data;
import lombok.NonNull;


@Data(staticConstructor = "of")
public class ResponseMessage {
    @NonNull
    private String message;
}
