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
package org.jclouds.dimensiondata.cloudcontroller.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jclouds.util.Predicates2.retry;

import java.util.Arrays;
import java.util.List;

import org.jclouds.dimensiondata.cloudcontroller.domain.FirewallRuleTarget.Port;
import org.jclouds.dimensiondata.cloudcontroller.domain.Property;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.Vlan;
import org.jclouds.dimensiondata.cloudcontroller.features.NetworkApi;
import org.jclouds.dimensiondata.cloudcontroller.features.ServerApi;
import org.jclouds.dimensiondata.cloudcontroller.predicates.ServerStatus;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class DimensionDataCloudControllerUtils {

    public static final String JCLOUDS_FW_RULE_PATTERN = "jclouds.%s";

    /*
    public static String analyseResponse(Response response) {
        if (!response.info().isEmpty()) {

        if ("NAME_NOT_UNIQUE".equals(response.responseCode())) {
            String operation = response.operation();
            if ("DEPLOY_NETWORK_DOMAIN".equals(operation)) {
                return "networkDomainId";
            } else if ("CREATE_FIREWALL_RULE".equals(operation)) {
                return "firewallRuleId";
            }
        }
    }
    */

    public static String tryFindPropertyValue(Response response, final String propertyName) {
        if (!response.info().isEmpty()) {
            Optional<String> optionalPropertyName = FluentIterable.from(response.info()).firstMatch(new Predicate<Property>() {
                @Override
                public boolean apply(Property input) {
                    return input.name().equals(propertyName);
                }
            }).transform(new Function<Property, String>() {
                @Override
                public String apply(Property input) {
                    return input.value();
                }
            });
            if (!optionalPropertyName.isPresent()) {
                // TODO
                throw new IllegalStateException();
            }
            return optionalPropertyName.get();
        }
        return "";
    }

    public static Optional<Vlan> tryGetVlan(NetworkApi api, String networkDomainId) {
        return api.listVlans(networkDomainId).concat().firstMatch(Predicates.<Vlan>notNull());
    }

    public static void waitForServerStatus(ServerApi api, String serverId, boolean started, boolean deployed, long timeoutMillis, String message) {
        boolean isServerRunning = retry(new ServerStatus(api, started, deployed), timeoutMillis).apply(serverId);
        if (!isServerRunning) {
            throw new IllegalStateException(message);
        }
    }

    // Helper function for simplifying an array of ports to a list of ranges FirewallOptions expects.
    public static List<Port> simplifyPorts(int[] ports){
        if ((ports == null) || (ports.length == 0)) {
            return null;
        }
        List<Port> output = Lists.newArrayList();
        Arrays.sort(ports);

        int range_start = ports[0];
        int range_end = ports[0];

        for (int i = 1; i < ports.length; i++) {
            if ((ports[i - 1] == ports[i] - 1) || (ports[i - 1] == ports[i])){
                // Range continues.
                range_end = ports[i];
            }
            else {
                // Range ends.
                output.addAll(formatRange(range_start, range_end));
                range_start = ports[i];
                range_end = ports[i];
            }
        }
        // Make sure we get the last range.
        output.addAll(formatRange(range_start, range_end));
        return output;
    }

    // Helper function for simplifyPorts. Formats port range strings.
    public static List<Port> formatRange(int range_start, int range_end) {
        List<Port> ports = Lists.newArrayList();
        checkArgument(range_start > 0);
        checkArgument(range_end < 65536);
        formatRange(range_start, range_end, ports);
        return ports;
    }

    // Helper function for simplifyPorts. Formats port range strings.
    public static List<Port> formatRange(int range_start, int range_end, List<Port> ports) {
        int allowed_range = 1024;

        if (range_end > 65535) {
            ports.add(Port.create(range_start, 65535));
        } else if (range_end - range_start > allowed_range) {
            while (range_start <= range_end) {
                formatRange(range_start, range_start + allowed_range, ports);
                range_start = range_start + allowed_range + 1;
            }
        } else {
            ports.add(Port.create(range_start, range_end));
        }
        return ports;
    }

    public static String generateFirewallName(Port destinationPort) {
        return String.format(JCLOUDS_FW_RULE_PATTERN, destinationPort.end() == null ? destinationPort.begin() : destinationPort.begin() + "_" + destinationPort.end());
    }
}
