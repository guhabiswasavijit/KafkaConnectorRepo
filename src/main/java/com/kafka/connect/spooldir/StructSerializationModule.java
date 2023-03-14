package com.kafka.connect.spooldir;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StructSerializationModule extends SimpleModule {
    private static final Logger log = LoggerFactory.getLogger(StructSerializationModule.class);

    public StructSerializationModule() {
        super();
        addSerializer(Struct.class, new Serializer());
        addDeserializer(Struct.class, new Deserializer());
    }

    public static class Storage {
        @JsonProperty(value = "schema")
        public Schema schema;
        @JsonProperty(value = "fieldValues")
        public List<KeyValue> fieldValues;

        public Struct build() {
            log.trace("build() - Creating struct for {}", this.schema);
            Struct struct = new Struct(this.schema);
            for (KeyValue keyValue : this.fieldValues) {
                log.trace("build() - Setting field value for '{}'", keyValue.name);
                struct.put(keyValue.name, keyValue.value());
            }
            struct.validate();
            return struct;
        }
    }

    static class Serializer extends JsonSerializer<Struct> {
        @Override
        public void serialize(Struct struct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            struct.validate();
            Storage result = new Storage();
            result.schema = struct.schema();
            result.fieldValues = new ArrayList<>();
            for (Field field : struct.schema().fields()) {
                log.trace("serialize() - Processing field '{}'", field.name());
                KeyValue keyValue = new KeyValue();
                keyValue.name = field.name();
                keyValue.schema = field.schema();
                keyValue.value(struct.get(field));
                result.fieldValues.add(keyValue);
            }
            jsonGenerator.writeObject(result);
        }
    }

    static class Deserializer extends JsonDeserializer<Struct> {

        @Override
        public Struct deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Storage storage = jsonParser.readValueAs(Storage.class);
            return storage.build();
        }
    }

}
