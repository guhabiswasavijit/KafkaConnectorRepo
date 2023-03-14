package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ValidUrl implements ConfigDef.Validator {

    static void validate(String config, String value) {
        try {
            new URL(value);
        } catch (MalformedURLException e) {
            ConfigException configException = new ConfigException(
                    config, value, "Could not parse to URL."
            );
            configException.initCause(e);

            throw configException;
        }
    }

    @Override
    public void ensureValid(String config, Object value) {
        if (value instanceof String) {
            validate(config, (String) value);
        } else if (value instanceof List) {
            List<String> values = (List<String>) value;
            for (String v : values) {
                validate(config, v);
            }
        } else {
            throw new ConfigException(config, value, "Must be a string or list.");
        }
    }

    @Override
    public String toString() {
        return "";
    }
}