package com.kafka.connect.spooldir;


import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import java.util.Map;

@Title("Binary File Source Connector")
@Description("This connector is used to read an entire file as a byte array write the data to Kafka.")

public class SpoolDirBinaryFileSourceConnector extends AbstractSourceConnector<SpoolDirBinaryFileSourceConnectorConfig> {
  @Override
  protected SpoolDirBinaryFileSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirBinaryFileSourceConnectorConfig(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirBinaryFileSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirBinaryFileSourceConnectorConfig.config();
  }
}
