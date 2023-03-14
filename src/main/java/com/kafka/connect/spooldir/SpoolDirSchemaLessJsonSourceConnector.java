package com.kafka.connect.spooldir;


import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;

import java.util.Map;

@Title("Schema Less Json Source Connector")
@Description("This connector is used to `stream <https://en.wikipedia.org/wiki/JSON_Streaming>_` JSON files from a directory. " +
    "This connector will read each file node by node writing each node as a record in Kafka." +
    "For example if your data file contains several json objects the connector will read from { to } " +
    "for each object and write each object to Kafka.")
public class SpoolDirSchemaLessJsonSourceConnector extends AbstractSourceConnector<SpoolDirSchemaLessJsonSourceConnectorConfig> {
  @Override
  protected SpoolDirSchemaLessJsonSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirSchemaLessJsonSourceConnectorConfig(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirSchemaLessJsonSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirSchemaLessJsonSourceConnectorConfig.config();
  }
}
