package com.jovialcode.parser.common;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.mongodb.source.reader.deserializer.MongoDeserializationSchema;
import org.bson.BsonDocument;

import java.util.Optional;

import static org.apache.flink.connector.mongodb.common.utils.MongoConstants.DEFAULT_JSON_WRITER_SETTINGS;

public class MongoJsonDeserializationSchema implements MongoDeserializationSchema<String> {

    @Override
    public String deserialize(BsonDocument document) {
        return Optional.ofNullable(document)
            .map(doc -> doc.toJson(DEFAULT_JSON_WRITER_SETTINGS))
            .orElse(null);
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
