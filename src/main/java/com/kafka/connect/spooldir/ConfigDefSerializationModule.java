package com.kafka.connect.spooldir;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.types.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ConfigDefSerializationModule extends SimpleModule {
    private static final Logger log = LoggerFactory.getLogger(ConfigDefSerializationModule.class);

    public ConfigDefSerializationModule() {
        super();
        addSerializer(Password.class, new Serializer());
        addDeserializer(Password.class, new Deserializer());
        addSerializer(ConfigDef.Validator.class, new JsonSerializer<ConfigDef.Validator>() {
            @Override
            public void serialize(ConfigDef.Validator validator, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(validator.toString());
            }
        });
    }

    static class Serializer extends JsonSerializer<Password> {
        @Override
        public void serialize(Password password, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(password.toString());
        }
    }

    static class Deserializer extends JsonDeserializer<Password> {
        @Override
        public Password deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String text = jsonParser.readValueAs(String.class);
            return new Password(text);
        }
    }
}
