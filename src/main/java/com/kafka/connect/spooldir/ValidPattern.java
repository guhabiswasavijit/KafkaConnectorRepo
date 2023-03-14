package com.kafka.connect.spooldir;

import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPattern implements ConfigDef.Validator {
    final Pattern pattern;

    public static ValidPattern of(String pattern) {
        Pattern regexPattern = Pattern.compile(pattern);
        return of(regexPattern);
    }

    public static ValidPattern of(Pattern pattern) {
        return new ValidPattern(pattern);
    }

    private ValidPattern(Pattern pattern) {
        Preconditions.checkNotNull(pattern, "pattern cannot be null");
        this.pattern = pattern;
    }

    @Override
    public void ensureValid(String s, Object o) {
        if (null == o || !(o instanceof String)) {
            throw new ConfigException(s, "Must be a string and cannot be null.");
        }
        Matcher matcher = this.pattern.matcher((String) o);
        if (!matcher.matches()) {
            throw new ConfigException(
                    s,
                    String.format("'%s' does not match pattern '%s'.", o, pattern.pattern())
            );
        }
    }

    @Override
    public String toString() {
        return String.format("Matches regex( %s )", this.pattern.pattern());
    }
}
