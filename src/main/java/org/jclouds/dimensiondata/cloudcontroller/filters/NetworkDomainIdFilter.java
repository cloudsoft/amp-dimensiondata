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
package org.jclouds.dimensiondata.cloudcontroller.filters;

import com.google.common.base.Supplier;
import org.jclouds.dimensiondata.cloudcontroller.compute.options.DimensionDataCloudControllerTemplateOptions;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;

import javax.inject.Inject;

/**
 * Adds Network Domain ID from TemplateOptions to the request.
 */
public class NetworkDomainIdFilter implements HttpRequestFilter {
   @Inject
   protected Supplier<DimensionDataCloudControllerTemplateOptions> templateOptionsSupplier;

   @Override public HttpRequest filter(HttpRequest request) throws HttpException {
      DimensionDataCloudControllerTemplateOptions templateOptions = templateOptionsSupplier.get();
      if (templateOptions != null && templateOptions.getNetworkDomainId() != null) {
         return request.toBuilder().addQueryParam("networkDomainId", templateOptions.getNetworkDomainId()).build();
      } else {
         return request;
      }
   }
}
