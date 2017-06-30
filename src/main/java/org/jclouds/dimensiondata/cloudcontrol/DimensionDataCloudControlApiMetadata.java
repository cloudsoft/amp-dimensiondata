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
package org.jclouds.dimensiondata.cloudcontrol;

import static org.jclouds.compute.config.ComputeServiceProperties.TEMPLATE;
import static org.jclouds.reflect.Reflection2.typeToken;

import java.net.URI;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.dimensiondata.cloudcontrol.compute.config.DimensionDataCloudControlComputeServiceContextModule;
import org.jclouds.dimensiondata.cloudcontrol.config.DimensionDataCloudControlHttpApiModule;
import org.jclouds.dimensiondata.cloudcontrol.config.DimensionDataCloudControlParserModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

public class DimensionDataCloudControlApiMetadata extends BaseHttpApiMetadata<DimensionDataCloudControlApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public DimensionDataCloudControlApiMetadata() {
      this(new Builder());
   }

   protected DimensionDataCloudControlApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = BaseHttpApiMetadata.defaultProperties();
      properties.setProperty(Constants.PROPERTY_CONNECTION_TIMEOUT, "1200000"); // 15 minutes
      properties.put(TEMPLATE, "imageNameMatches=Ubuntu,osVersionMatches=1[456]\\.[01][04](\\.[0-9])?-LTS");
      return properties;
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<DimensionDataCloudControlApi, Builder> {

      protected Builder() {
         id("dimensiondata-cloudcontrol")
                 .name("DimensionData CloudController API")
                 .identityName("user name")
                 .credentialName("user password")
                 .documentation(URI.create("http://www.dimensiondata.com/en-US/Solutions/Cloud"))
                 .defaultEndpoint("https://api-REGION.dimensiondata.com/caas").version("2.4")
                 .defaultProperties(DimensionDataCloudControlApiMetadata.defaultProperties())
                 .view(typeToken(ComputeServiceContext.class))
                 .defaultModules(ImmutableSet.<Class<? extends Module>>builder()
                         .add(DimensionDataCloudControlHttpApiModule.class)
                         .add(DimensionDataCloudControlParserModule.class)
                         .add(DimensionDataCloudControlComputeServiceContextModule.class)
                         .build());
      }

      @Override
      public DimensionDataCloudControlApiMetadata build() {
         return new DimensionDataCloudControlApiMetadata(this);
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
