/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hudi.keygen;

import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.common.config.TypedProperties;

import org.apache.avro.generic.GenericRecord;

import java.util.Arrays;
import java.util.List;

/**
 * Simple key generator, which takes names of fields to be used for recordKey and partitionPath as configs.
 */
public class SimpleKeyGenerator extends BuiltinKeyGenerator {

  protected final String recordKeyField;

  protected final String partitionPathField;

  protected final boolean hiveStylePartitioning;

  protected final boolean encodePartitionPath;

  public SimpleKeyGenerator(TypedProperties props) {
    this(props, props.getString(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY()));
  }

  public SimpleKeyGenerator(TypedProperties props, String partitionPathField) {
    super(props);
    this.recordKeyField = props.getString(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY());
    this.partitionPathField = partitionPathField;
    this.hiveStylePartitioning = props.getBoolean(DataSourceWriteOptions.HIVE_STYLE_PARTITIONING_OPT_KEY(),
        Boolean.parseBoolean(DataSourceWriteOptions.DEFAULT_HIVE_STYLE_PARTITIONING_OPT_VAL()));
    this.encodePartitionPath = props.getBoolean(DataSourceWriteOptions.URL_ENCODE_PARTITIONING_OPT_KEY(),
        Boolean.parseBoolean(DataSourceWriteOptions.DEFAULT_URL_ENCODE_PARTITIONING_OPT_VAL()));
  }

  @Override
  public String getRecordKey(GenericRecord record) {
    return KeyGenUtils.getRecordKey(record, recordKeyField);
  }

  @Override
  public String getPartitionPath(GenericRecord record) {
    return KeyGenUtils.getPartitionPath(record, partitionPathField, hiveStylePartitioning, encodePartitionPath);
  }

  @Override
  public List<String> getRecordKeyFields() {
    return Arrays.asList(recordKeyField);
  }

  @Override
  public List<String> getPartitionPathFields() {
    return Arrays.asList(partitionPathField);
  }
}