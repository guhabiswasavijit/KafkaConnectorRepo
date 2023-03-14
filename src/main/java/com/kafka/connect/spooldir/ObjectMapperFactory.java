package com.kafka.connect.spooldir;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
    public static final ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();
        INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        INSTANCE.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        INSTANCE.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        INSTANCE.registerModule(new SchemaSerializationModule());
        INSTANCE.registerModule(new StructSerializationModule());
        INSTANCE.registerModule(new SinkRecordSerializationModule());
        INSTANCE.registerModule(new SourceRecordSerializationModule());
        INSTANCE.registerModule(new TimeSerializationModule());
        INSTANCE.registerModule(new HeaderSerializationModule());
        INSTANCE.registerModule(new ConfigDefSerializationModule());
    }

}