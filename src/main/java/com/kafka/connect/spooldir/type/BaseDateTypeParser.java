package com.kafka.connect.spooldir.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import org.apache.kafka.connect.data.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class BaseDateTypeParser implements TypeParser {
  final static Logger log = LoggerFactory.getLogger(BaseDateTypeParser.class);
  protected final TimeZone timeZone;
  final SimpleDateFormat[] dateFormats;

  public BaseDateTypeParser(TimeZone timeZone, SimpleDateFormat... dateFormats) {
    this.dateFormats = dateFormats;
    this.timeZone = timeZone;
  }

  abstract Date process(Date date);

  @Override
  public Object parseString(String s, final Schema schema) {
    Date date = null;
    for (SimpleDateFormat dateFormat : this.dateFormats) {
      try {
        date = dateFormat.parse(s);
        break;
      } catch (ParseException e) {
        if (log.isTraceEnabled()) {
          log.trace("Could not parse '{}' to java.util.Date", s, e);
        }
      }
    }

    Preconditions.checkState(null != date, "Could not parse '%s' to java.util.Date", s);
    return process(date);
  }


  @Override
  public Object parseJsonNode(JsonNode input, Schema schema) {
    Date result;
    if (input.isNumber()) {
      result = new java.util.Date(input.longValue());
    } else if (input.isTextual()) {
      result = (Date) parseString(input.textValue(), schema);
    } else {
      throw new IllegalStateException(
          String.format(
              "NodeType:%s '%s' could not be converted to %s",
              input.getNodeType(),
              input.textValue(),
              expectedClass().getSimpleName()
          )
      );
    }

    return process(result);
  }

  @Override
  public Class<?> expectedClass() {
    return java.util.Date.class;
  }
}
