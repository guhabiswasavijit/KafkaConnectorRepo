/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kafka.connect.spooldir;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import java.util.Map;

@Title("Line Delimited Source Connector")
@Description("This connector is used to read a file line by line and write the data to Kafka.")

public class SpoolDirLineDelimitedSourceConnector extends AbstractSourceConnector<SpoolDirLineDelimitedSourceConnectorConfig> {
  @Override
  protected SpoolDirLineDelimitedSourceConnectorConfig config(Map<String, ?> settings) {
    return new SpoolDirLineDelimitedSourceConnectorConfig(settings);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SpoolDirLineDelimitedSourceTask.class;
  }

  @Override
  public ConfigDef config() {
    return SpoolDirLineDelimitedSourceConnectorConfig.config();
  }
}
