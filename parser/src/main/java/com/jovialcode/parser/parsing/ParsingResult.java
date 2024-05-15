package com.jovialcode.parser.parsing;


import java.io.Serializable;

public class ParsingResult implements Serializable {
    private static final long serialVersionUID = 3L;

    private String tag;
    private String value;

    public ParsingResult() {
    }

    public ParsingResult(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
