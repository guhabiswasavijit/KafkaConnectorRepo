package com.kafka.connect.spooldir;

import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidHostnameAndPort implements ConfigDef.Validator {
    private static final Logger log = LoggerFactory.getLogger(ValidHostnameAndPort.class);

    static final Pattern HOSTNAME_PATTERN = Pattern.compile("^(.+)\\:(\\d{1,5})$");

    @Override
    public void ensureValid(String key, Object value) {
        log.trace("ensureValid('{}', '{}')", key, value);
        if (value instanceof String) {
            try {
                Matcher matcher = HOSTNAME_PATTERN.matcher((CharSequence) value);
                Preconditions.checkState(matcher.matches(), "'%s' does not match pattern '%s'.", key, HOSTNAME_PATTERN.pattern());
                final int port = Integer.parseInt(matcher.group(2));
                Preconditions.checkState(port >= 1 && port <= 65535, "'%s' port value %s is out of range. Port must be between 1 and 65535.", key, port);
            } catch (Exception ex) {
                throw new ConfigException(
                        String.format("'%s' is not a valid hostname and port.", key),
                        ex
                );
            }
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            for (String s : list) {
                ensureValid(key, s);
            }
        } else {
            throw new ConfigException(
                    String.format("'%s' must be a string or a list.", key)
            );
        }
    }


    public static ValidHostnameAndPort of() {
        return new ValidHostnameAndPort();
    }
}


