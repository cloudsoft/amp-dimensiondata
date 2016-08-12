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
package org.jclouds.dimensiondata.cloudcontroller;

import static org.jclouds.reflect.Reflection2.typeToken;

import java.net.URI;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.dimensiondata.cloudcontroller.compute.config.DimensionDataCloudControllerComputeServiceContextModule;
import org.jclouds.dimensiondata.cloudcontroller.config.DimensionDataCloudControllerHttpApiModule;
import org.jclouds.dimensiondata.cloudcontroller.config.DimensionDataCloudControllerParserModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class DimensionDataCloudControllerApiMetadata extends BaseHttpApiMetadata<DimensionDataCloudControllerApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public DimensionDataCloudControllerApiMetadata() {
      this(new Builder());
   }

   protected DimensionDataCloudControllerApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = BaseHttpApiMetadata.defaultProperties();
      properties.setProperty(Constants.PROPERTY_CONNECTION_TIMEOUT, "1200000"); // 15 minutes
      return properties;
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<DimensionDataCloudControllerApi, Builder> {

      protected Builder() {
         id("dimensiondata-cloudcontroller").name("DimensionData CloudController API").identityName("user name")
               .credentialName("user password")
               .documentation(URI.create("http://www.dimensiondata.com/en-US/Solutions/Cloud"))
               .defaultEndpoint("https://api-REGION.dimensiondata.com/").version("2.2")
               .defaultProperties(DimensionDataCloudControllerApiMetadata.defaultProperties())
               .view(typeToken(ComputeServiceContext.class)).defaultModules(
               ImmutableSet.<Class<? extends Module>>builder().add(DimensionDataCloudControllerHttpApiModule.class)
                     .add(DimensionDataCloudControllerParserModule.class)
                     .add(DimensionDataCloudControllerComputeServiceContextModule.class).build());
      }

      @Override
      public DimensionDataCloudControllerApiMetadata build() {
         return new DimensionDataCloudControllerApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }

      @Override
      public Builder fromApiMetadata(ApiMetadata in) {
         return this;
      }
   }
}
