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

import java.util.List;

import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class NetworkInfo {

    public static Builder builder() {
        return new AutoValue_NetworkInfo.Builder();
    }

    NetworkInfo() {} // For AutoValue only!

    public abstract String networkDomainId();
    public abstract NIC primaryNic();
    public abstract List<NIC> additionalNic();

    @SerializedNames({ "networkDomainId", "primaryNic", "additionalNic" })
    public static NetworkInfo create(String networkDomainId, NIC primaryNic, List<NIC> additionalNic) {
        return builder().networkDomainId(networkDomainId).primaryNic(primaryNic).additionalNic(additionalNic).build();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder networkDomainId(String networkDomainId);
        public abstract Builder primaryNic(NIC primaryNic);
        public abstract Builder additionalNic(List<NIC> additionalNic);

        public abstract NetworkInfo build();
    }
}
