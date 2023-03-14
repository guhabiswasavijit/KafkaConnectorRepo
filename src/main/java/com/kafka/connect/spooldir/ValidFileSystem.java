package com.kafka.connect.spooldir;

import com.google.common.base.Strings;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Validator is used as a base for validators that check file system properties.
 */
public abstract class ValidFileSystem implements ConfigDef.Validator {
    private static final Logger log = LoggerFactory.getLogger(ValidFileSystem.class);

    public final boolean ensureWritable;

    protected ValidFileSystem(boolean ensureWritable) {
        this.ensureWritable = ensureWritable;
    }

    protected abstract void ensureValid(String setting, Object input, File file);

    @Override
    public void ensureValid(String setting, Object input) {
        log.trace("ensureValid('{}', '{}')", setting, input);
        if (!(input instanceof String)) {
            throw new ConfigException(setting, "Input must be a string.");
        }
        final String value = input.toString();
        if (Strings.isNullOrEmpty(value)) {
            throw new ConfigException(setting, "Cannot be null or empty.");
        }
        final File file = new File(value);
        if (!file.isAbsolute()) {
            throw new ConfigException(
                    setting,
                    String.format("File '%s' is not an absolute path.", file)
            );
        }
        ensureValid(setting, input, file);

        if (this.ensureWritable) {
            if (!file.canWrite()) {
                throw new ConfigException(
                        setting,
                        String.format("File '%s' should be writable.", file)
                );
            }
        }

        if (!file.canRead()) {
            throw new ConfigException(
                    setting,
                    String.format("File '%s' should be readable.", file)
            );
        }
    }
}