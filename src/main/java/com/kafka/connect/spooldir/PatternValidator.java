package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class PatternValidator implements ConfigDef.Validator {

    static void validatePattern(String setting, String pattern) {
        try {
            Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            throw new ConfigException(
                    setting,
                    pattern,
                    String.format(
                            "Could not compile regex '%s'.",
                            pattern
                    )
            );
        }
    }


    @Override
    public void ensureValid(String setting, Object value) {
        if (value instanceof String) {
            String s = (String) value;
            validatePattern(setting, s);
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            for (String s : list) {
                validatePattern(setting, s);
            }
        } else {
            throw new ConfigException(setting, value, "value must be a String or List.");
        }
    }
}
