package com.kafka.connect.spooldir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class ValidFileWritable extends ValidFile {
    private static final Logger log = LoggerFactory.getLogger(ValidFileWritable.class);

    private ValidFileWritable() {
        super(true);
    }

    @Override
    protected void ensureValid(String setting, Object input, File directoryPath) {
        super.ensureValid(setting, input, directoryPath);
    }

    public static ValidFileWritable of() {
        return new ValidFileWritable();
    }

    @Override
    public String toString() {
        return "Absolute path to a file that exists and is writable.";
    }
}
