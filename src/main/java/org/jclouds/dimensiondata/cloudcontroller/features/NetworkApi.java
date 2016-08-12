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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.collect.PagedIterable;
import org.jclouds.dimensiondata.cloudcontroller.domain.FirewallRule;
import org.jclouds.dimensiondata.cloudcontroller.domain.FirewallRuleTarget;
import org.jclouds.dimensiondata.cloudcontroller.domain.FirewallRuleTarget.Port;
import org.jclouds.dimensiondata.cloudcontroller.domain.NatRule;
import org.jclouds.dimensiondata.cloudcontroller.domain.NetworkDomain;
import org.jclouds.dimensiondata.cloudcontroller.domain.PaginatedCollection;
import org.jclouds.dimensiondata.cloudcontroller.domain.Placement;
import org.jclouds.dimensiondata.cloudcontroller.domain.PublicIpBlock;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.Vlan;
import org.jclouds.dimensiondata.cloudcontroller.filters.OrganisationIdFilter;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseFirewallRules;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseNatRules;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseNetworkDomains;
import org.jclouds.dimensiondata.cloudcontroller.options.PaginationOptions;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParsePublicIpBlocks;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseVlans;
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
@Path("/caas/{jclouds.api-version}/network")
public interface NetworkApi {


    @Named("network:deployNetworkDomain")
    @POST
    @Path("/deployNetworkDomain")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response deployNetworkDomain(@PayloadParam("datacenterId") String datacenterId, @PayloadParam("name") String name, @PayloadParam("description") String description, @PayloadParam("type") String type);

    @Named("server:getNetworkDomain")
    @GET
    @Path("/networkDomain/{id}")
    @Fallback(NullOnNotFoundOr404.class)
    NetworkDomain getNetworkDomain(@PathParam("id") String networkDomainId);

    @Named("network:list")
    @GET
    @Path("/networkDomain")
    @ResponseParser(ParseNetworkDomains.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<NetworkDomain> listNetworkDomains(PaginationOptions options);

    @Named("network:list")
    @GET
    @Path("/networkDomain")
    @Transform(ParseNetworkDomains.ToPagedIterable.class)
    @ResponseParser(ParseNetworkDomains.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<NetworkDomain> listNetworkDomains();

    @Named("network:deleteNetwotkDomain")
    @POST
    @Path("/deleteNetworkDomain")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(NullOnNotFoundOr404.class)
    Response deleteNetworkDomain(@PayloadParam("id") String networkDomainId);

    @Named("network:deployVlan")
    @POST
    @Path("/deployVlan")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response deployVlan(@PayloadParam("networkDomainId") String networkDomainId, @PayloadParam("name") String name, @PayloadParam("description") String description,
          @PayloadParam("privateIpv4BaseAddress") String privateIpv4BaseAddress, @PayloadParam("privateIpv4PrefixSize") Integer privateIpv4PrefixSize);

    @Named("server:getVlan")
    @GET
    @Path("/vlan/{id}")
    @Fallback(NullOnNotFoundOr404.class)
    Vlan getVlan(@PathParam("id") String vlanId);

    @Named("network:vlan")
    @GET
    @Path("/vlan")
    @ResponseParser(ParseVlans.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<Vlan> listVlans(@QueryParam("networkDomainId") String networkDomainId, PaginationOptions options);

    @Named("network:vlan")
    @GET
    @Path("/vlan")
    @Transform(ParseVlans.ToPagedIterable.class)
    @ResponseParser(ParseVlans.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<Vlan> listVlans(@QueryParam("networkDomainId") String networkDomainId);

    @Named("network:deleteVlan")
    @POST
    @Path("/deleteVlan")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(NullOnNotFoundOr404.class)
    Response deleteVlan(@PayloadParam("id") String vlanId);

    @Named("network:addPublicIpBlock")
    @POST
    @Path("/addPublicIpBlock")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response addPublicIpBlock(@PayloadParam("networkDomainId") String networkDomainId);

    @Named("network:publicIpBlock")
    @GET
    @Path("/publicIpBlock")
    @ResponseParser(ParsePublicIpBlocks.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<PublicIpBlock> listPublicIPv4AddressBlocks(@QueryParam("networkDomainId") String networkDomainId, PaginationOptions options);

    @Named("network:publicIpBlock")
    @GET
    @Path("/publicIpBlock")
    @Transform(ParsePublicIpBlocks.ToPagedIterable.class)
    @ResponseParser(ParsePublicIpBlocks.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<PublicIpBlock> listPublicIPv4AddressBlocks(@QueryParam("networkDomainId") String networkDomainId);

    @Named("network:removePublicIpBlock")
    @POST
    @Path("/removePublicIpBlock")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(NullOnNotFoundOr404.class)
    Response removePublicIpBlock(@PayloadParam("id") String publicIpBlockId);

    @Named("server:getPublicIpBlock")
    @GET
    @Path("/publicIpBlock/{id}")
    @Fallback(NullOnNotFoundOr404.class)
    PublicIpBlock getPublicIPv4AddressBlock(@PathParam("id") String publicIPv4AddressBlockId);

    @Named("network:createNatRule")
    @POST
    @Path("/createNatRule")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(Fallbacks.VoidOnNotFoundOr404.class)
    Response createNatRule(@PayloadParam("networkDomainId") String networkDomainId, @PayloadParam("internalIp") String internalIp,
          @PayloadParam("externalIp") String externalIp);

    @Named("network:natRule")
    @GET
    @Path("/natRule")
    @ResponseParser(ParseNatRules.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<NatRule> listNatRules(@QueryParam("networkDomainId") String networkDomainId, PaginationOptions options);

    @Named("network:natRule")
    @GET
    @Path("/natRule")
    @Transform(ParseNatRules.ToPagedIterable.class)
    @ResponseParser(ParseNatRules.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<NatRule> listNatRules(@QueryParam("networkDomainId") String networkDomainId);

    @Named("server:getNatRule")
    @GET
    @Path("/natRule/{id}")
    @Fallback(NullOnNotFoundOr404.class)
    NatRule getNatRule(@PathParam("id") String natRuleId);

    @Named("network:deleteNatRule")
    @POST
    @Path("/deleteNatRule")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response deleteNatRule(@PayloadParam("id") String natRuleId);

    @Named("network:createFirewallRule")
    @POST
    @Path("/createFirewallRule")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response createFirewallRule(@PayloadParam("networkDomainId") String networkDomainId, @PayloadParam("name") String name,
          @PayloadParam("action") String action, @PayloadParam("ipVersion") String ipVersion, @PayloadParam("protocol") String protocol,
          @PayloadParam("source") FirewallRuleTarget source, @PayloadParam("destination") FirewallRuleTarget destination, @PayloadParam("enabled") Boolean enabled,
          @PayloadParam("placement") Placement placement);

    @Named("network:listFirewallRules")
    @GET
        @Path("/firewallRule")
    @ResponseParser(ParseFirewallRules.class)
    @Fallback(Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404.class)
    PaginatedCollection<FirewallRule> listFirewallRules(@QueryParam("networkDomainId") String networkDomainId,
          PaginationOptions options);

    @Named("network:listFirewallRules")
    @GET
    @Path("/firewallRule")
    @Transform(ParseFirewallRules.ToPagedIterable.class)
    @ResponseParser(ParseFirewallRules.class)
    @Fallback(Fallbacks.EmptyPagedIterableOnNotFoundOr404.class)
    PagedIterable<FirewallRule> listFirewallRules(@QueryParam("networkDomainId") String networkDomainId);

    @Named("network:deleteFirewallRule")
    @POST
    @Path("/deleteFirewallRule")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(NullOnNotFoundOr404.class)
    Response deleteFirewallRule(@PayloadParam("id") String natRuleId);

    @Named("network:createPortList")
    @POST
    @Path("/createPortList")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    Response createPortList(@PayloadParam("networkDomainId") String networkDomainId, @PayloadParam("name") String name,
          @PayloadParam("description") String description, @PayloadParam("port") List<Port> port, @PayloadParam("childPortListId") List<String> childPortListId);

    @Named("network:getPortList")
    @GET
    @Path("/portList/{id}")
    @Fallback(NullOnNotFoundOr404.class)
    FirewallRuleTarget.PortList getPortList(@PathParam("id") String portListId);

    @Named("network:deletePortList")
    @POST
    @Path("/deletePortList")
    @Produces(MediaType.APPLICATION_JSON)
    @MapBinder(BindToJsonPayload.class)
    @Fallback(NullOnNotFoundOr404.class)
    Response deletePortList(@PayloadParam("id") String portListId);

}
