package com.kafka.connect.spooldir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Validator is used to ensure that the setting is a directory on the file system, is readable, and writable.
 */
public class ValidDirectoryWritable extends ValidDirectory {
    private static final Logger log = LoggerFactory.getLogger(ValidDirectoryWritable.class);

    private ValidDirectoryWritable() {
        super(true);
    }

    @Override
    protected void ensureValid(String setting, Object input, File directoryPath) {
        super.ensureValid(setting, input, directoryPath);
    }


    public static ValidDirectoryWritable of() {
        return new ValidDirectoryWritable();
    }

    @Override
    public String toString() {
        return "Absolute path to a directory that exists and is writable.";
    }
}
