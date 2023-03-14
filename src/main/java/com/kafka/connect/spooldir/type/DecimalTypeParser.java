package com.kafka.connect.spooldir.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.DataException;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class DecimalTypeParser implements TypeParser {
  static final String NOT_FOUND_MESSAGE = String.format(
      "Invalid Decimal schema: %s parameter not found.",
      Decimal.SCALE_FIELD
  );
  static final String NOT_PARSABLE_MESSAGE = String.format(
      "Invalid Decimal schema: %s parameter could not be converted to an integer.",
      Decimal.SCALE_FIELD
  );
  final Cache<Schema, Integer> schemaCache;

  public DecimalTypeParser() {
    this.schemaCache = CacheBuilder.newBuilder()
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .build();
  }

  private static int scaleInternal(Schema schema) {
    if (null == schema.parameters()) {
      throw new DataException(NOT_FOUND_MESSAGE);
    }

    String scaleString = schema.parameters().get(Decimal.SCALE_FIELD);
    if (scaleString == null) {
      throw new DataException(NOT_FOUND_MESSAGE);
    } else {
      try {
        return Integer.parseInt(scaleString);
      } catch (NumberFormatException var3) {
        throw new DataException(NOT_PARSABLE_MESSAGE, var3);
      }
    }
  }

  int scale(final Schema schema) {
    int scale;
    try {
      scale = this.schemaCache.get(schema, () -> scaleInternal(schema));
    } catch (Exception e) {
      throw new DataException(e);
    }
    return scale;
  }

  @Override
  public Object parseString(String s, Schema schema) {
    int scale = scale(schema);
    return new BigDecimal(s).setScale(scale);
  }

  @Override
  public Class<?> expectedClass() {
    return BigDecimal.class;
  }

  @Override
  public Object parseJsonNode(JsonNode input, Schema schema) {
    Object result;

    if (input.isNumber()) {
      int scale = scale(schema);
      result = input.decimalValue().setScale(scale);
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
