package com.kafka.connect.spooldir.type;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.connect.data.Schema;

public class Int32TypeParser implements TypeParser {
  @Override
  public Object parseString(String s, final Schema schema) {
    return Integer.parseInt(s);
  }

  @Override
  public Class<?> expectedClass() {
    return Integer.class;
  }

  @Override
  public Object parseJsonNode(JsonNode input, Schema schema) {
    Object result;
    if (input.isNumber()) {
      result = input.intValue();
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
