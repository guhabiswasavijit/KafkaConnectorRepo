package com.kafka.connect.spooldir;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ValidEnum implements ConfigDef.Validator {
    final Set<String> validEnums;
    final Class<?> enumClass;

    /**
     * Method is used to create a new INSTANCE of the enum validator.
     *
     * @param enumClass Enum class with the entries to validate for.
     * @param excludes  Enum entries to exclude from the validator.
     * @return ValidEnum
     * @see com.github.jcustenborder.kafka.connect.utils.config.validators.Validators#validEnum(Class, Enum[])
     */
    public static ValidEnum of(Class<?> enumClass, String... excludes) {
        return new ValidEnum(enumClass, excludes);
    }

    private ValidEnum(Class<?> enumClass, String... excludes) {
        Preconditions.checkNotNull(enumClass, "enumClass cannot be null");
        Preconditions.checkState(enumClass.isEnum(), "enumClass must be an enum.");
        Set<String> validEnums = new LinkedHashSet<>();
        for (Object o : enumClass.getEnumConstants()) {
            String key = o.toString();
            validEnums.add(key);
        }
        validEnums.removeAll(Arrays.asList(excludes));
        this.validEnums = validEnums;
        this.enumClass = enumClass;
    }

    @Override
    public void ensureValid(String s, Object o) {

        if (o instanceof String) {
            if (!validEnums.contains(o)) {
                throw new ConfigException(
                        s,
                        String.format(
                                "'%s' is not a valid value for %s. Valid values are %s.",
                                o,
                                enumClass.getSimpleName(),
                                ConfigUtils.enumValues(enumClass)
                        )
                );
            }
        } else if (o instanceof List) {
            List list = (List) o;
            for (Object i : list) {
                ensureValid(s, i);
            }
        } else {
            throw new ConfigException(
                    s,
                    o,
                    "Must be a String or List"
            );
        }


    }

    @Override
    public String toString() {
        return "Matches: ``" + Joiner.on("``, ``").join(this.validEnums) + "``";
    }
}

