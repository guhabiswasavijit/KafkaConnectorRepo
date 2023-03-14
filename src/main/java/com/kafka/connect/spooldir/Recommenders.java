package com.kafka.connect.spooldir;

import com.google.common.collect.ImmutableList;
import org.apache.kafka.common.config.ConfigDef;

import java.nio.charset.Charset;

public class Recommenders {
    private Recommenders() {

    }

    /**
     * Method is used to return a recommender that will mark a ConfigItem as visible if
     * the configKey is set to the specified value.
     *
     * @param configKey The setting to retrieve the value from.
     * @param value     The expected value.
     * @return recommender
     */
    public static ConfigDef.Recommender visibleIf(String configKey, Object value) {
        return new VisibleIfRecommender(configKey, value, ValidValuesCallback.EMPTY);
    }

    /**
     * Method is used to return a recommender that will mark a ConfigItem as visible if
     * the configKey is set to the specified value.
     *
     * @param configKey           The setting to retrieve the value from.
     * @param value               The expected value.
     * @param validValuesCallback The expected value.
     * @return recommender
     */
    public static ConfigDef.Recommender visibleIf(String configKey, Object value, ValidValuesCallback validValuesCallback) {
        return new VisibleIfRecommender(configKey, value, validValuesCallback);
    }

    public static ConfigDef.Recommender enumValues(Class<?> enumClass, String... excludes) {
        return enumValues(enumClass, VisibleCallback.ALWAYS_VISIBLE, excludes);
    }

    public static ConfigDef.Recommender enumValues(Class<?> enumClass, VisibleCallback visible, String... excludes) {
        return new EnumRecommender(enumClass, visible, excludes);
    }

    public static ConfigDef.Recommender charset() {
        return charset(VisibleCallback.ALWAYS_VISIBLE, Charset.availableCharsets().keySet());
    }

    public static ConfigDef.Recommender charset(VisibleCallback visible) {
        return charset(visible, Charset.availableCharsets().keySet());
    }

    public static ConfigDef.Recommender charset(VisibleCallback visible, String... charsets) {
        return charset(visible, ImmutableList.copyOf(charsets));
    }

    public static ConfigDef.Recommender charset(VisibleCallback visible, Iterable<String> charsets) {
        return new CharsetRecommender(charsets, visible);
    }
}
