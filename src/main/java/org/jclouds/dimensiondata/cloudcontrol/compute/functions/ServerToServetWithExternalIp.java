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
package org.jclouds.dimensiondata.cloudcontrol.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.compute.reference.ComputeServiceConstants.COMPUTE_LOGGER;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.NatRule;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.internal.ServerWithExternalIp;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public class ServerToServetWithExternalIp implements Function<Server, ServerWithExternalIp> {

    @Resource
    @Named(COMPUTE_LOGGER)
    protected Logger logger = Logger.NULL;

    private final DimensionDataCloudControlApi api;

    @Inject
    public ServerToServetWithExternalIp(DimensionDataCloudControlApi api) {
        this.api = checkNotNull(api, "api");
    }

    @Override
    public ServerWithExternalIp apply(final Server server) {
        if (server == null) return null;
        ServerWithExternalIp.Builder builder = ServerWithExternalIp.builder().server(server);

        Optional<NatRule> natRuleOptional = api.getNetworkApi().listNatRules(server.networkInfo().networkDomainId()).concat()
                .firstMatch(new Predicate<NatRule>() {
                    @Override
                    public boolean apply(NatRule input) {
                        return input.internalIp().equalsIgnoreCase(server.networkInfo().primaryNic().privateIpv4());
                    }
                });

        if (natRuleOptional.isPresent()) {
            builder.externalIp(natRuleOptional.get().externalIp());
        }
        return builder.build();
    }
}
