package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import java.util.Map;

@Title("Json Source Connector")
@Description("This connector is used to `stream <https://en.wikipedia.org/wiki/JSON_Streaming>` JSON files from a directory " +
    "while converting the data based on the schema supplied in the configuration.")
public class SpoolDirJsonSourceConnector extends AbstractSpoolDirSourceConnector<SpoolDirJsonSourceConnectorConfig> {
  @Override
  protected SpoolDirJsonSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirJsonSourceConnectorConfig(false, settings);
  }

  @Override
  protected AbstractSchemaGenerator<SpoolDirJsonSourceConnectorConfig> generator(Map<String, String> settings) {
    return new JsonSchemaGenerator(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirJsonSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirJsonSourceConnectorConfig.config();
  }
}
