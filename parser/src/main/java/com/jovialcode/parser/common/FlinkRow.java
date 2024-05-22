package com.jovialcode.parser.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlinkRow<T> implements Serializable {
    private static final long serialVersionUID = 444L;
    @JsonProperty("before")
    @JsonDeserialize(using = FlinkRowDeserializer.class)
    private T before;
    @JsonProperty("after")
    @JsonDeserialize(using = FlinkRowDeserializer.class)
    private T after;

    @JsonProperty("ts_ms")
    private Long timestamp;

    public FlinkRow() {
    }

    public FlinkRow(T before, T after, Long timestamp) {
        this.before = before;
        this.after = after;
        this.timestamp = timestamp;
    }

    public T getBefore() {
        return before;
    }

    public T getAfter() {
        return after;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
