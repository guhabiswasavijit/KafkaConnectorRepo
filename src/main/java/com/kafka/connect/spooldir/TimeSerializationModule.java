package com.kafka.connect.spooldir;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.common.utils.Time;

import java.io.IOException;
import java.util.function.Supplier;

public class TimeSerializationModule extends SimpleModule {

    public TimeSerializationModule() {
        super();
        addSerializer(Time.class, new Serializer());
        addDeserializer(Time.class, new Deserializer());
    }

    public static class Storage {
        public long milliseconds;
        public long nanoseconds;
        public long hiResClockMs;

        public Storage() {

        }

        public Storage(Time time) {
            this.milliseconds = time.milliseconds();
            this.nanoseconds = time.nanoseconds();
            this.hiResClockMs = time.hiResClockMs();
        }
    }

    static class Serializer extends JsonSerializer<Time> {
        @Override
        public void serialize(Time time, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            Storage storage = new Storage(time);
            jsonGenerator.writeObject(storage);
        }
    }

    static class Deserializer extends JsonDeserializer<Time> {

        @Override
        public Time deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final Storage storage = jsonParser.readValueAs(Storage.class);

            return new Time() {

                @Override
                public void waitObject(Object o, Supplier<Boolean> supplier, long l) throws InterruptedException {

                }

                @Override
                public long milliseconds() {
                    return storage.milliseconds;
                }

                @Override
                public long hiResClockMs() {
                    return storage.hiResClockMs;
                }

                @Override
                public long nanoseconds() {
                    return storage.nanoseconds;
                }

                @Override
                public void sleep(long l) {
                    try {
                        Thread.sleep(l);
                    } catch (InterruptedException e) {

                    }
                }
            };
        }
    }
}
