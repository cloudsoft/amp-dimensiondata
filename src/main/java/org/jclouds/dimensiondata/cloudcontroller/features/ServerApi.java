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

import java.util.List;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks;
import org.jclouds.collect.PagedIterable;
import org.jclouds.dimensiondata.cloudcontroller.domain.Disk;
import org.jclouds.dimensiondata.cloudcontroller.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.Server;
import org.jclouds.dimensiondata.cloudcontroller.domain.options.CreateServerOptions;
import org.jclouds.dimensiondata.cloudcontroller.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontroller.filters.OrganisationIdFilter;
import org.jclouds.dimensiondata.cloudcontroller.options.PaginationOptions;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseServers;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.Transform;
import org.jclouds.rest.binders.BindToJsonPayload;

@RequestFilters({BasicAuthentication.class, OrganisationIdFilter.class})
@Consumes(MediaType.APPLICATION_JSON)
@Path("/caas/{jclouds.api-version}/server")
public interface ServerApi {

    @Named("server:list")
    @GET
    @Path("/server")
    @ResponseParser(ParseServers.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<Server> listServers(PaginationOptions options);

    @Named("server:list")
    @GET
    @Path("/server")
    @Transform(ParseServers.ToPagedIterable.class)
    @ResponseParser(ParseServers.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<Server> listServers();

    @Named("server:get")
    @GET
    @Path("/server/{id}")
    @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    Server getServer(@PathParam("id") String id);

    @Named("server:deploy")
    @POST
    @Path("/deployServer")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response deployServer(@PayloadParam("name") String name, @PayloadParam("imageId") String imageId, @PayloadParam("start") Boolean start,
          @PayloadParam("networkInfo") NetworkInfo networkInfo, @PayloadParam("disk") List<Disk> disks, @PayloadParam("administratorPassword") String administratorPassword);

    @Named("server:deploy")
    @POST
    @Path("/deployServer")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(CreateServerOptions.class)
    Response deployServer(@PayloadParam("name") String name, @PayloadParam("imageId") String imageId, @PayloadParam("start") Boolean start,
          @PayloadParam("networkInfo") NetworkInfo networkInfo, @PayloadParam("disk") List<Disk> disks, @PayloadParam("administratorPassword") String administratorPassword,
          CreateServerOptions options);

    @Named("server:delete")
    @POST
    @Path("/deleteServer")
    @Produces(MediaType.APPLICATION_JSON)
    @Fallback(Fallbacks.VoidOnNotFoundOr404.class)
    @MapBinder(BindToJsonPayload.class)
    Response deleteServer(@PayloadParam("id") String id);

    @Named("server:powerOffServer")
    @POST
    @Path("/powerOffServer")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response powerOffServer(@PayloadParam("id") String id);

    @Named("server:rebootServer")
    @POST
    @Path("/rebootServer")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response rebootServerServer(@PayloadParam("id") String id);

}
