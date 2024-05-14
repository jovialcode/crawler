package com.jovialcode.parser.parsing;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParsingResult implements Serializable {
    private static final long serialVersionUID = 3L;

    private final String tag;
    private final String value;

    public ParsingResult(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }
}
