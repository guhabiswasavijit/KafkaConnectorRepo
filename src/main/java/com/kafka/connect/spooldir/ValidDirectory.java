package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigException;

import java.io.File;

/**
 * Validator is used to ensure that the setting is a directory on the file system and that it is readable.
 */
public class ValidDirectory extends ValidFileSystem {

    private ValidDirectory() {
        this(false);
    }

    protected ValidDirectory(boolean ensureWritable) {
        super(ensureWritable);
    }


    @Override
    protected void ensureValid(String setting, Object input, File file) {
        if (!file.isDirectory()) {
            throw new ConfigException(
                    setting,
                    String.format("'%s' must be a directory.", file)
            );
        }
    }

    public static ValidDirectory of() {
        return new ValidDirectory();
    }

    @Override
    public String toString() {
        return "Absolute path to a directory that exists.";
    }
}