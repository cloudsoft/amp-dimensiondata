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
package org.jclouds.dimensiondata.cloudcontrol.domain;

import java.util.List;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Monitoring {

   Monitoring() {
   }

   @Nullable
   public abstract List<Property> properties();

   public abstract String maintenanceStatus();

   @SerializedNames({ "property", "maintenanceStatus" })
   public static Monitoring create(List<Property> properties, String maintenanceStatus) {
      return builder().properties(properties).maintenanceStatus(maintenanceStatus).build();
   }

   public abstract Builder toBuilder();

   @AutoValue.Builder
   public abstract static class Builder {

      public abstract Builder properties(List<Property> properties);

      public abstract Builder maintenanceStatus(String maintenanceStatus);

      abstract Monitoring autoBuild();

      abstract List<Property> properties();

      public Monitoring build() {
         properties(properties() != null ? ImmutableList.copyOf(properties()) : null);
         return autoBuild();
      }
   }

   public static Builder builder() {
      return new AutoValue_Monitoring.Builder();
   }
}
