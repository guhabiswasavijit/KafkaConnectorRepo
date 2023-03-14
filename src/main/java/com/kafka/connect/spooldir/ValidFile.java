package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigException;

import java.io.File;

/**
 * Validator is used to ensure that the setting is a file on the file system and that it is readable.
 */
public class ValidFile extends ValidFileSystem {

    private ValidFile() {
        this(false);
    }

    protected ValidFile(boolean ensureWritable) {
        super(ensureWritable);
    }

    @Override
    protected void ensureValid(String setting, Object input, File file) {
        if (!file.isFile()) {
            throw new ConfigException(
                    setting,
                    String.format("'%s' must be a file.", file)
            );
        }
    }

    public static ValidFile of() {
        return new ValidFile();
    }

    @Override
    public String toString() {
        return "Absolute path to a file that exists.";
    }
}


