package com.jovialcode.parser.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlinkData<T> implements Serializable {
    @JsonProperty("_id")
    private Object id;
    @JsonProperty("operationType")
    private String operationType;

    @JsonProperty("fullDocument")
    @JsonDeserialize(using = FlinkDocumentDeserializer.class)
    private T fullDocument;

    @JsonProperty("fullDocumentBeforeChange")
    @JsonDeserialize(using = FlinkDocumentDeserializer.class)
    private T fullDocumentBeforeChange;

    @JsonProperty("ts_ms")
    private Long timestamp;

    public FlinkData() {
    }

    public FlinkData(Object id, String operationType, T fullDocument, T fullDocumentBeforeChange, Long timestamp) {
        this.id = id;
        this.operationType = operationType;
        this.fullDocument = fullDocument;
        this.fullDocumentBeforeChange = fullDocumentBeforeChange;
        this.timestamp = timestamp;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public T getFullDocument() {
        return fullDocument;
    }

    public void setFullDocument(T fullDocument) {
        this.fullDocument = fullDocument;
    }

    public T getFullDocumentBeforeChange() {
        return fullDocumentBeforeChange;
    }

    public void setFullDocumentBeforeChange(T fullDocumentBeforeChange) {
        this.fullDocumentBeforeChange = fullDocumentBeforeChange;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
