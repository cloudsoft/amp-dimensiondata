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
package org.jclouds.dimensiondata.cloudcontroller.features;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks;
import org.jclouds.collect.PagedIterable;
import org.jclouds.dimensiondata.cloudcontroller.domain.Datacenter;
import org.jclouds.dimensiondata.cloudcontroller.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontroller.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontroller.filters.DatacenterIdListDatacentersFilter;
import org.jclouds.dimensiondata.cloudcontroller.filters.OrganisationIdFilter;
import org.jclouds.dimensiondata.cloudcontroller.options.PaginationOptions;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseDatacenters;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseOperatingSystems;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.Transform;

@RequestFilters({BasicAuthentication.class, OrganisationIdFilter.class})
@Consumes(MediaType.APPLICATION_JSON)
@Path("/caas/{jclouds.api-version}/infrastructure")
public interface InfrastructureApi {

    @Named("infrastructure:datacenter")
    @GET
    @Path("/datacenter")
    @ResponseParser(ParseDatacenters.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<Datacenter> listDatacenters(PaginationOptions options);

    @Named("infrastructure:datacenter")
    @GET
    @Path("/datacenter")
    @Transform(ParseDatacenters.ToPagedIterable.class)
    @ResponseParser(ParseDatacenters.class)
    @RequestFilters(DatacenterIdListDatacentersFilter.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<Datacenter> listDatacenters();

    @Named("infrastructure:operatingSystem")
    @GET
    @Path("/operatingSystem")
    @ResponseParser(ParseOperatingSystems.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<OperatingSystem> listOperatingSystems(@QueryParam("datacenterId") String datacenterId,
          PaginationOptions options);

    @Named("infrastructure:operatingSystem")
    @GET
    @Path("/operatingSystem")
    @Transform(ParseOperatingSystems.ToPagedIterable.class)
    @ResponseParser(ParseOperatingSystems.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<OperatingSystem> listOperatingSystems(@QueryParam("datacenterId") String datacenterId);

}
