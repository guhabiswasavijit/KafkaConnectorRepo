package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import java.util.Map;

@Title("CSV Source Connector")
@Description("The SpoolDirCsvSourceConnector will monitor the directory specified in `input.path` for files and read them as a CSV " +
    "converting each of the records to the strongly typed equivalent specified in `key.schema` and `value.schema`.")
public class SpoolDirCsvSourceConnector extends AbstractSpoolDirSourceConnector<SpoolDirCsvSourceConnectorConfig> {
  @Override
  protected SpoolDirCsvSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirCsvSourceConnectorConfig(false, settings);
  }

  @Override
  protected AbstractSchemaGenerator<SpoolDirCsvSourceConnectorConfig> generator(Map<String, String> settings) {
    return new CsvSchemaGenerator(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirCsvSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirCsvSourceConnectorConfig.config();
  }
}
