package com.kafka.connect.spooldir;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ValidValuesCallback {
    List<Object> validValues(String configItem, Map<String, Object> settings);

    ValidValuesCallback EMPTY = (configItem, settings) -> Collections.emptyList();
}