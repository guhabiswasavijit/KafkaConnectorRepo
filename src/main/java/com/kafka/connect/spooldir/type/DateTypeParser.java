package com.kafka.connect.spooldir.type;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTypeParser extends BaseDateTypeParser {
  public DateTypeParser() {
    this(TimeZone.getTimeZone("UTC"), new SimpleDateFormat("yyyy-MM-dd"));
  }

  public DateTypeParser(TimeZone timeZone, SimpleDateFormat... dateFormats) {
    super(timeZone, dateFormats);
  }

  @Override
  Date process(Date date) {
    Calendar calendar = Calendar.getInstance(this.timeZone);
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }
}
