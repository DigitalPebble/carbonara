/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.  The ASF licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.digitalpebble.module.ccf;

import com.digitalpebble.Columns;
import com.digitalpebble.EnrichmentModule;
import org.apache.spark.sql.Row;

import static com.digitalpebble.Columns.ENERGY_USED;

/**
 * Provides an estimate of energy used for networking in and out of data centres.
 * Applies a flat coefficient per Gb
 * @see <a href="https://www.cloudcarbonfootprint.org/docs/methodology#networking">CCF methodology</a>
 **/
public class Networking implements EnrichmentModule {

    // estimated kWh/Gb
    private final double network_coefficient = 0.001;

    @Override
    public Columns[] columnsAdded() {
        return new Columns[]{ENERGY_USED};
    }

    @Override
    public Row process(Row row) {

        int index = row.fieldIndex("line_item_usage_type");
        String usageType = row.getString(index);
        // SHORTCUT
        if (usageType == null || !usageType.endsWith("-Bytes")) {
            return row;
        }
        // TODO apply only to rows corresponding to networking in or out of a region

        // get the amount of data transferred
        index = row.fieldIndex("line_item_usage_amount");
        double amount_gb = row.getDouble(index);
        double energy_gb = amount_gb * network_coefficient;

        return EnrichmentModule.withUpdatedValue(row, ENERGY_USED.getLabel(), energy_gb);
    }
}
