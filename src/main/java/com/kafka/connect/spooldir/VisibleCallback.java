package com.kafka.connect.spooldir;

import java.util.Map;

public interface VisibleCallback {
    boolean visible(String configItem, Map<String, Object> settings);

    VisibleCallback ALWAYS_VISIBLE = (configItem, settings) -> true;
}
