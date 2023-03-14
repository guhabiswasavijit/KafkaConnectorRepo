package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;

import java.util.Map;

@Title("Avro Source Connector")
@Description("This connector is used to read avro data files from the file system and write their contents " +
    "to Kafka. The schema of the file is used to read the data and produce it to Kafka")
public class SpoolDirAvroSourceConnector extends AbstractSourceConnector<SpoolDirAvroSourceConnectorConfig> {
  @Override
  protected SpoolDirAvroSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirAvroSourceConnectorConfig(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirAvroSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirAvroSourceConnectorConfig.config();
  }
}
