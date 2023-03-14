package com.kafka.connect.spooldir.type;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.connect.data.Schema;

public class StringTypeParser implements TypeParser {
  @Override
  public Object parseString(String s, final Schema schema) {
    return s;
  }

  @Override
  public Class<?> expectedClass() {
    return String.class;
  }

  @Override
  public Object parseJsonNode(JsonNode input, Schema schema) {
    return input.textValue();
  }
}
