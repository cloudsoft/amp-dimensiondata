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
package org.jclouds.dimensiondata.cloudcontroller.compute.functions;

import static java.lang.String.format;
import static org.jclouds.dimensiondata.cloudcontroller.utils.DimensionDataCloudControllerUtils.generateFirewallRuleName;
import static org.jclouds.dimensiondata.cloudcontroller.utils.DimensionDataCloudControllerUtils.manageResponse;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.compute.reference.ComputeServiceConstants.Timeouts;
import org.jclouds.dimensiondata.cloudcontroller.DimensionDataCloudControllerApi;
import org.jclouds.dimensiondata.cloudcontroller.domain.FirewallRule;
import org.jclouds.dimensiondata.cloudcontroller.domain.NatRule;
import org.jclouds.dimensiondata.cloudcontroller.domain.PublicIpBlock;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.Server;
import org.jclouds.dimensiondata.cloudcontroller.utils.DimensionDataCloudControllerUtils;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

@Singleton
public class CleanupServer implements Function<String, Boolean> {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   protected final DimensionDataCloudControllerApi api;
   private final Timeouts timeouts;

   @Inject
   public CleanupServer(DimensionDataCloudControllerApi api, Timeouts timeouts) {
      this.api = api;
      this.timeouts = timeouts;
   }

   @Override
   public Boolean apply(final String serverId) {
      Server server = api.getServerApi().getServer(serverId);
      if (server == null) return true;
      String networkDomainId = server.networkInfo().networkDomainId();
      final String internalIp = server.networkInfo().primaryNic().privateIpv4();

      // delete nat rules associated to the server, if any
      List<NatRule> natRulesToBeDeleted = api.getNetworkApi().listNatRules(networkDomainId).concat().filter(new Predicate<NatRule>() {
         @Override
         public boolean apply(NatRule natRule) {
            return natRule.internalIp().equals(internalIp);
         }
      }).toList();

      for (final NatRule natRule : natRulesToBeDeleted) {
         Response deleteNatRuleResponse = api.getNetworkApi().deleteNatRule(natRule.id());
         manageResponse(deleteNatRuleResponse, format("Cannot delete NAT rule for internalIp (%s) - externalIp %s created for server (%s). Rolling back ...", natRule.id(), natRule.internalIp(), natRule.externalIp(), serverId));

         Optional<PublicIpBlock> optionalPublicIpBlock = api.getNetworkApi().listPublicIPv4AddressBlocks(networkDomainId).concat().firstMatch(new Predicate<PublicIpBlock>() {
            @Override
            public boolean apply(PublicIpBlock input) {
               return input.baseIp().equals(natRule.externalIp());
            }
         });
         if (optionalPublicIpBlock.isPresent()) {
            Response deleteIpBlockResponse = api.getNetworkApi().removePublicIpBlock(optionalPublicIpBlock.get().id());
            manageResponse(deleteIpBlockResponse, format("Cannot delete ip block address for externalIp (%s) created for server (%s). Rolling back ...", natRule.externalIp(), serverId));
         }
      }

      // delete firewall rules
      List<FirewallRule> firewallRulesToBeDeleted = api.getNetworkApi().listFirewallRules(
            networkDomainId).concat().filter(new Predicate<FirewallRule>() {
         @Override
         public boolean apply(FirewallRule firewallRule) {
            return firewallRule.name().equals(generateFirewallRuleName(serverId));
         }
      }).toList();

      for (FirewallRule firewallRule : firewallRulesToBeDeleted) {
         Response deleteFirewallRuleResponse = api.getNetworkApi().deleteFirewallRule(firewallRule.id());
         manageResponse(deleteFirewallRuleResponse, format("Cannot delete firewall rule %s created for server (%s). Rolling back ...", firewallRule.id(), serverId));
      }

      for (FirewallRule firewallRule : firewallRulesToBeDeleted) {
         if (firewallRule.destination() != null && firewallRule.destination().portList() != null) {
            Response deletePortListResponse = api.getNetworkApi().deletePortList(firewallRule.destination().portList().id());
            manageResponse(deletePortListResponse, format("Cannot delete port list %s created for server (%s). Rolling back ...", firewallRule.destination().portList().id(), serverId));
         }
      }

      // power off the server
      Response powerOffResponse = api.getServerApi().powerOffServer(serverId);
      manageResponse(powerOffResponse, format("Cannot power off the server %s.", serverId));
      String message = format("server(%s) not terminated within %d ms.", serverId, timeouts.nodeTerminated);
      DimensionDataCloudControllerUtils.waitForServerStatus(api.getServerApi(), serverId, false, true, timeouts.nodeTerminated, message);

      // delete server
      Response deleteServerResponse = api.getServerApi().deleteServer(serverId);
      return deleteServerResponse.error().isEmpty();
   }



}
