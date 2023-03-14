package com.kafka.connect.spooldir.type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeTypeParser extends BaseDateTypeParser {
  public TimeTypeParser(TimeZone timeZone, SimpleDateFormat... dateFormats) {
    super(timeZone, dateFormats);
  }

  public TimeTypeParser() {
    this(TimeZone.getTimeZone("UTC"), new SimpleDateFormat("HH:mm:ss"));
  }

  @Override
  Date process(Date date) {
    return date;
  }
}