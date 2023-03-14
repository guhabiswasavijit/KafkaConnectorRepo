package com.kafka.connect.spooldir;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionUtil {
    private static final Logger log = LoggerFactory.getLogger(VersionUtil.class);
    final static String FALLBACK_VERSION = "0.0.0.0";

    public static String version(Class<?> cls) {
        String result;

        try {
            result = cls.getPackage().getImplementationVersion();

            if (Strings.isNullOrEmpty(result)) {
                result = FALLBACK_VERSION;
            }
        } catch (Exception ex) {
            log.error("Exception thrown while getting error", ex);
            result = FALLBACK_VERSION;
        }
        return result;
    }

}