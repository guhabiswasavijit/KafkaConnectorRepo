package com.kafka.connect.spooldir;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.connect.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeaderSerializationModule extends SimpleModule {
    private static final Logger log = LoggerFactory.getLogger(HeaderSerializationModule.class);

    public HeaderSerializationModule() {
        super();
        addSerializer(Header.class, new Serializer());
        addDeserializer(Header.class, new Deserializer());
    }

    static class Serializer extends JsonSerializer<Header> {
        @Override
        public void serialize(Header header, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            KeyValue storage = new KeyValue();
            storage.name = header.key();
            storage.schema = header.schema();
            storage.value(header.value());
            jsonGenerator.writeObject(storage);
        }
    }

    static class Deserializer extends JsonDeserializer<Header> {
        @Override
        public Header deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            KeyValue storage = jsonParser.readValueAs(KeyValue.class);
            return new HeaderImpl(storage.name, storage.schema, storage.value());
        }
    }
}
