/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.  The ASF licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.digitalpebble.carbonara.modules;

import com.digitalpebble.carbonara.CarbonaraColumn;
import com.digitalpebble.carbonara.Column;
import com.digitalpebble.carbonara.EnrichmentModule;
import org.apache.spark.sql.Row;

import java.util.Map;

/** Populate the field CPU_load with a constant value (50 by default) as percentage of the CPU load **/
public class ConstantLoad implements EnrichmentModule {

    private double load_value = 50d;

    @Override
    public void init(Map<String, String> params) {
        // TODO set a different value via configuration
    }

    @Override
    public Column[] columnsAdded() {
        return new Column[]{CarbonaraColumn.CPU_LOAD};
    }

    @Override
    public Row process(Row row) {
        return EnrichmentModule.withUpdatedValue(row, CarbonaraColumn.CPU_LOAD, load_value);
    }
}
