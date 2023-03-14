package com.kafka.connect.spooldir;

import com.kafka.connect.spooldir.type.DateTypeParser;
import com.kafka.connect.spooldir.type.TimeTypeParser;
import com.kafka.connect.spooldir.type.TimestampTypeParser;
import com.kafka.connect.spooldir.type.TypeParser;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractSpoolDirSourceTask<CONF extends AbstractSpoolDirSourceConnectorConfig> extends AbstractSourceTask<CONF> {
  private static final Logger log = LoggerFactory.getLogger(AbstractSpoolDirSourceTask.class);
  protected Parser parser;

  @Override
  public void start(Map<String, String> settings) {
    super.start(settings);

    this.parser = new Parser();
    Map<Schema, TypeParser> dateTypeParsers = ImmutableMap.of(
        Timestamp.SCHEMA, new TimestampTypeParser(this.config.parserTimestampTimezone, this.config.parserTimestampDateFormats),
        Date.SCHEMA, new DateTypeParser(this.config.parserTimestampTimezone, this.config.parserTimestampDateFormats),
        Time.SCHEMA, new TimeTypeParser(this.config.parserTimestampTimezone, this.config.parserTimestampDateFormats)
    );

    for (Map.Entry<Schema, TypeParser> kvp : dateTypeParsers.entrySet()) {
      this.parser.registerTypeParser(kvp.getKey(), kvp.getValue());
    }
  }

  protected void addRecord(List<SourceRecord> records, SchemaAndValue key, SchemaAndValue value) {
    final Long timestamp;

    switch (this.config.timestampMode) {
      case FIELD:
        Struct valueStruct = (Struct) value.value();
        log.trace("addRecord() - Reading date from timestamp field '{}'", this.config.timestampField);
        final java.util.Date date = (java.util.Date) valueStruct.get(this.config.timestampField);
        timestamp = date.getTime();
        break;
      case FILE_TIME:
        timestamp = this.inputFile.lastModified();
        break;
      case PROCESS_TIME:
        timestamp = null;
        break;
      default:
        throw new UnsupportedOperationException(
            String.format("Unsupported timestamp mode. %s", this.config.timestampMode)
        );
    }

    SourceRecord sourceRecord = record(
        key,
        value,
        timestamp
    );
    recordCount++;
    records.add(sourceRecord);
  }

}