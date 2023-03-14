package com.kafka.connect.spooldir;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.header.Header;

class HeaderImpl implements Header {
    final String key;
    final Schema schema;
    final Object value;

    HeaderImpl(String key, Schema schema, Object value) {
        this.key = key;
        this.schema = schema;
        this.value = value;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Schema schema() {
        return this.schema;
    }

    @Override
    public Object value() {
        return this.value;
    }

    @Override
    public Header with(Schema schema, Object value) {
        return new HeaderImpl(this.key, schema, value);
    }

    @Override
    public Header rename(String s) {
        return new HeaderImpl(this.key, this.schema, this.value);
    }
}
