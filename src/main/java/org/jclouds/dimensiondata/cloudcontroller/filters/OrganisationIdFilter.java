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

import java.util.List;

import org.jclouds.collect.Memoized;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Accepts requests and modifies the endpoint path so that it is injected with the organisation id.
 * Handles both oec and caas based URLs.
 */
public class OrganisationIdFilter implements HttpRequestFilter {

   private final Supplier<String> organisationIdSupplier;

   @Inject
   public OrganisationIdFilter(@Memoized Supplier<String> organisationIdSupplier) {
      this.organisationIdSupplier = organisationIdSupplier;
   }

   @Override public HttpRequest filter(HttpRequest request) throws HttpException {
      return request.toBuilder().replacePath(injectOrganisationId(request.getEndpoint().getPath())).build();
   }

   @VisibleForTesting
   public String injectOrganisationId(String path) {
      String organisationId = organisationIdSupplier.get();
      List<String> list = Lists.newArrayList(Splitter.on("/").split(path));
      list.add(3, organisationId);
      return Joiner.on("/").join(list);
   }

}
