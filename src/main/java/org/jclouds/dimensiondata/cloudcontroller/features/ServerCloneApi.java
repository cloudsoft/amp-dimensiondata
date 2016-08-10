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

import org.jclouds.Fallbacks;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.http.filters.BasicAuthentication;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.RequestFilters;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@RequestFilters({BasicAuthentication.class})
@Consumes(MediaType.APPLICATION_XML)
@Path("/oec/0.9/{org-id}")
public interface ServerCloneApi
{

    @Named("server:clone")
    @GET
    @Path("/server/{id}")
    @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    Response clone(@PathParam("org-id") String organisationId, @PathParam("id") String id, @QueryParam("clone") String newImageName, @QueryParam("desc") String newImageDescription);

}
