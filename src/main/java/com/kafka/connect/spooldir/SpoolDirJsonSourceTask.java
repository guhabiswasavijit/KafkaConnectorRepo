package com.kafka.connect.spooldir;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpoolDirJsonSourceTask extends AbstractSpoolDirSourceTask<SpoolDirJsonSourceConnectorConfig> {
  private static final Logger log = LoggerFactory.getLogger(SpoolDirJsonSourceTask.class);
  private JsonFactory jsonFactory;
  private JsonParser jsonParser;
  private Iterator<JsonNode> iterator;
  private long offset;

  @Override
  protected SpoolDirJsonSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirJsonSourceConnectorConfig(true, settings);
  }

  @Override
  public void start(Map<String, String> settings) {
    super.start(settings);
    this.jsonFactory = new JsonFactory();
  }

  @Override
  protected void configure(InputFile inputFile, Long lastOffset) throws IOException {
    if (null != jsonParser) {
      log.trace("configure() - Closing existing json parser.");
      jsonParser.close();
    }
    InputStream inputStream = inputFile.openStream();
    this.jsonParser = this.jsonFactory.createParser(inputStream);
    this.iterator = ObjectMapperFactory.INSTANCE.readValues(this.jsonParser, JsonNode.class);
    this.offset = -1;

    if (null != lastOffset) {
      int skippedRecords = 1;
      while (this.iterator.hasNext() && skippedRecords <= lastOffset) {
        next();
        skippedRecords++;
      }
      log.trace("configure() - Skipped {} record(s).", skippedRecords);
      log.info("configure() - Starting on offset {}", this.offset);
    }

  }

  JsonNode next() {
    this.offset++;
    return this.iterator.next();
  }

  @Override
  protected List<SourceRecord> process() {
    List<SourceRecord> records = new ArrayList<>(this.config.batchSize);

    while (this.iterator.hasNext() && records.size() < this.config.batchSize) {
      JsonNode node = next();

      Struct valueStruct = new Struct(this.config.valueSchema);
      Struct keyStruct = new Struct(this.config.keySchema);
      log.trace("process() - input = {}", node);
      for (Field field : this.config.valueSchema.fields()) {
        JsonNode fieldNode = node.get(field.name());
        log.trace("process() - field: {} input = '{}'", field.name(), fieldNode);
        Object fieldValue;
        try {
          fieldValue = this.parser.parseJsonNode(field.schema(), fieldNode);
          log.trace("process() - field: {} output = '{}'", field.name(), fieldValue);
          valueStruct.put(field, fieldValue);

          Field keyField = this.config.keySchema.field(field.name());
          if (null != keyField) {
            log.trace("process() - Setting key field '{}' to '{}'", keyField.name(), fieldValue);
            keyStruct.put(keyField, fieldValue);
          }
        } catch (Exception ex) {
          String message = String.format("Exception thrown while parsing data for '%s'. linenumber=%s", field.name(), this.recordOffset());
          throw new DataException(message, ex);
        }
      }

      addRecord(
          records,
          new SchemaAndValue(keyStruct.schema(), keyStruct),
          new SchemaAndValue(valueStruct.schema(), valueStruct)
      );
    }

    return records;
  }

  @Override
  protected long recordOffset() {
    return this.offset;
  }
}
