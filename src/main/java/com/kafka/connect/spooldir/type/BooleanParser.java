package com.kafka.connect.spooldir.type;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.connect.data.Schema;

public class BooleanParser implements TypeParser {
  @Override
  public Object parseString(String s, final Schema schema) {
    return Boolean.parseBoolean(s);
  }

  @Override
  public Class<?> expectedClass() {
    return Boolean.class;
  }

  @Override
  public Object parseJsonNode(JsonNode input, Schema schema) {
    Object result;
    if (input.isBoolean()) {
      result = input.booleanValue();
    } else if (input.isTextual()) {
      result = parseString(input.textValue(), schema);
    } else {
      throw new UnsupportedOperationException(
          String.format(
              "Could not parse '%s' to %s",
              input,
              this.expectedClass().getSimpleName()
          )
      );
    }
    return result;
  }
}
