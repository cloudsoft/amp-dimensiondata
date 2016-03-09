/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.dimensiondata.cloudcontroller.domain;

import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Datacenter {

    public static Builder builder() {
        return new AutoValue_Datacenter.Builder();
    }

    Datacenter() {} // For AutoValue only!

    public abstract String id();
    public abstract String displayName();
    public abstract String type();
    public abstract String city();
    public abstract String state();
    public abstract String country();
    public abstract String vpnUrl();
    public abstract String ftpsHost();

    @SerializedNames({ "id", "displayName", "type", "city", "state", "country", "vpnUrl", "ftpsHost" })
    public static Datacenter create(String id, String displayName, String type, String city, String state, String country, String vpnUrl, String ftpsHost) {
        return builder().id(id).displayName(displayName).type(type).city(city).state(state).country(country).vpnUrl(vpnUrl).ftpsHost(ftpsHost).build();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder displayName(String displayName);
        public abstract Builder type(String type);
        public abstract Builder city(String city);
        public abstract Builder state(String state);
        public abstract Builder country(String country);
        public abstract Builder vpnUrl(String vpnUrl);
        public abstract Builder ftpsHost(String ftpsHost);

        public abstract Datacenter build();
    }

}
