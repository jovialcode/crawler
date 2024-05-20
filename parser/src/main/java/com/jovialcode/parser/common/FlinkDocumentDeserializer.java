package com.jovialcode.parser.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class FlinkDocumentDeserializer<T> extends StdDeserializer<T> implements ContextualDeserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public FlinkDocumentDeserializer() {
        this(null);
    }

    public FlinkDocumentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode flinkDataNode = jp.getCodec().readTree(jp);
        return objectMapper.readValue(flinkDataNode.asText(), ctxt.getTypeFactory().constructType(this._valueClass));
    }

    @Override
    public JsonDeserializer<T> createContextual(DeserializationContext ctxt, BeanProperty property) {
        return new FlinkDocumentDeserializer<>(property.getType().getRawClass());
    }
}
