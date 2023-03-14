package com.kafka.connect.spooldir;

import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;

import java.util.List;
import java.util.Map;

class VisibleIfRecommender implements ConfigDef.Recommender {
    final String configKey;
    final Object value;
    final ValidValuesCallback validValuesCallback;

    VisibleIfRecommender(String configKey, Object value, ValidValuesCallback validValuesCallback) {
        Preconditions.checkNotNull(configKey, "configKey cannot be null.");
        Preconditions.checkNotNull(value, "value cannot be null.");
        Preconditions.checkNotNull(validValuesCallback, "validValuesCallback cannot be null.");
        this.configKey = configKey;
        this.value = value;
        this.validValuesCallback = validValuesCallback;
    }

    @Override
    public List<Object> validValues(String s, Map<String, Object> map) {
        return this.validValuesCallback.validValues(s, map);
    }

    @Override
    public boolean visible(String key, Map<String, Object> settings) {
        Object v = settings.get(this.configKey);
        return this.value.equals(v);
    }
}