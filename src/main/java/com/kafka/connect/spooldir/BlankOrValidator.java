package com.kafka.connect.spooldir;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.kafka.common.config.ConfigDef;

class BlankOrValidator implements ConfigDef.Validator {
    final ConfigDef.Validator validator;

    BlankOrValidator(ConfigDef.Validator validator) {
        Preconditions.checkNotNull(validator, "validator cannot be null.");
        this.validator = validator;
    }

    @Override
    public void ensureValid(String key, Object o) {
        if (o instanceof String) {
            final String s = o.toString();
            if (!Strings.isNullOrEmpty(s)) {
                validator.ensureValid(key, o);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Empty String or %s", this.validator);
    }

    public static ConfigDef.Validator of(ConfigDef.Validator validator) {
        return new BlankOrValidator(validator);
    }
}
