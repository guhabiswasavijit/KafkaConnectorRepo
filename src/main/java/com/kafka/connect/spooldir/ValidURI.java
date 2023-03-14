package com.kafka.connect.spooldir;


import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

class ValidURI implements ConfigDef.Validator {
    final Set<String> validSchemes;

    public ValidURI() {
        this(new String[0]);
    }

    public ValidURI(String... schemes) {
        validSchemes = ImmutableSet.copyOf(schemes);
    }

    void validate(String config, String value) {
        try {
            final URI uri = new URI(value);

            if (!validSchemes.isEmpty() && !validSchemes.contains(uri.getScheme())) {
                throw new ConfigException(
                        config,
                        value,
                        String.format(
                                "Scheme must be one of the following. '%s'",
                                Joiner.on("', '").join(this.validSchemes)
                        )
                );
            }


        } catch (URISyntaxException e) {
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
}