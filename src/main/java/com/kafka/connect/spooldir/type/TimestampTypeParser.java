package com.kafka.connect.spooldir.type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimestampTypeParser extends BaseDateTypeParser {
  public TimestampTypeParser(TimeZone timeZone, SimpleDateFormat... dateFormats) {
    super(timeZone, dateFormats);
  }

  public TimestampTypeParser() {
    this(TimeZone.getTimeZone("UTC"), new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
  }

  @Override
  Date process(Date date) {
    return date;
  }
}
