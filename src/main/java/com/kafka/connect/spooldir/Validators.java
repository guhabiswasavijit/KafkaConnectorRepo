package com.kafka.connect.spooldir;

import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef.Validator;
import org.apache.kafka.common.config.ConfigException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class Validators {
    private Validators() {

    }
    public static Validator pattern() {
        return new PatternValidator();
    }
    public static Validator patternMatches(String pattern) {
        return ValidPattern.of(pattern);
    }
    public static Validator patternMatches(Pattern pattern) {
        return ValidPattern.of(pattern);
    }
    public static Validator blankOr(Validator validator) {
        Preconditions.checkNotNull(validator, "validator cannot be null.");
        return BlankOrValidator.of(validator);
    }
    public static Validator validURI(String... schemes) {
        return new ValidURI(schemes);
    }
    public static Validator validUrl() {
        return new ValidUrl();
    }
    public static Validator validCharset(String... charsets) {
        if (null == charsets || charsets.length == 0) {
            return new ValidCharset();
        } else {
            return new ValidCharset(charsets);
        }
    }
    public static Validator validCharset() {
        return new ValidCharset();
    }

    public static Validator validEnum(Class<? extends Enum> enumClass, Enum... excludes) {
        String[] ex = new String[excludes.length];
        for (int i = 0; i < ex.length; i++) {
            ex[i] = excludes[i].toString();
        }
        return ValidEnum.of(enumClass, ex);
    }
    public static Validator validHostAndPort() {
        return ValidHostnameAndPort.of();
    }
    /**
     * Validator is used to ensure that the KeyStore type specified is valid.
     * @return
     */
    public static Validator validKeyStoreType() {
        return (s, o) -> {
            if (!(o instanceof String)) {
                throw new ConfigException(s, o, "Must be a string.");
            }

            String keyStoreType = o.toString();
            try {
                KeyStore.getInstance(keyStoreType);
            } catch (KeyStoreException e) {
                ConfigException exception = new ConfigException(s, o, "Invalid KeyStore type");
                exception.initCause(e);
                throw exception;
            }
        };
    }

    /**
     * Validator is used to ensure that the KeyManagerFactory Algorithm specified is valid.
     * @return
     */
    public static Validator validKeyManagerFactory() {
        return (s, o) -> {
            if (!(o instanceof String)) {
                throw new ConfigException(s, o, "Must be a string.");
            }

            String keyStoreType = o.toString();
            try {
                KeyManagerFactory.getInstance(keyStoreType);
            } catch (NoSuchAlgorithmException e) {
                ConfigException exception = new ConfigException(s, o, "Invalid Algorithm");
                exception.initCause(e);
                throw exception;
            }
        };
    }

    /**
     * Validator is used to ensure that the TrustManagerFactory Algorithm specified is valid.
     * @return
     */
    public static Validator validTrustManagerFactory() {
        return (s, o) -> {
            if (!(o instanceof String)) {
                throw new ConfigException(s, o, "Must be a string.");
            }

            String keyStoreType = o.toString();
            try {
                TrustManagerFactory.getInstance(keyStoreType);
            } catch (NoSuchAlgorithmException e) {
                ConfigException exception = new ConfigException(s, o, "Invalid Algorithm");
                exception.initCause(e);
                throw exception;
            }
        };
    }

    /**
     * Validator is used to ensure that the TrustManagerFactory Algorithm specified is valid.
     * @return
     */
    public static Validator validSSLContext() {
        return (s, o) -> {
            if (!(o instanceof String)) {
                throw new ConfigException(s, o, "Must be a string.");
            }

            String keyStoreType = o.toString();
            try {
                SSLContext.getInstance(keyStoreType);
            } catch (NoSuchAlgorithmException e) {
                ConfigException exception = new ConfigException(s, o, "Invalid Algorithm");
                exception.initCause(e);
                throw exception;
            }
        };
    }


}
