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
import static com.google.common.collect.FluentIterable.from;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.dimensiondata.cloudcontrol.domain.Server;
import org.jclouds.dimensiondata.cloudcontrol.domain.internal.ServerWithExternalIp;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.location.predicates.LocationPredicates;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@Singleton
public class ServerWithNatRuleToNodeMetadata implements Function<ServerWithExternalIp, NodeMetadata> {

    public static final Map<Server.State, NodeMetadata.Status> serverStateToNodeStatus = ImmutableMap
            .<Server.State, NodeMetadata.Status> builder()
            .put(Server.State.PENDING_DELETE, NodeMetadata.Status.PENDING)
            .put(Server.State.DELETED, NodeMetadata.Status.TERMINATED)
            .put(Server.State.NORMAL, NodeMetadata.Status.RUNNING)
            .put(Server.State.UNRECOGNIZED, NodeMetadata.Status.UNRECOGNIZED).build();

    private final Supplier<Set<? extends Location>> locations;
    private final GroupNamingConvention nodeNamingConvention;
    private final OsImageToImage osImageToImage;
    private final OsImageToHardware osImageToHardware;
    private final Map<String, Credentials> credentialStore;

    @Inject
    ServerWithNatRuleToNodeMetadata(@Memoized Supplier<Set<? extends Location>> locations,
                               GroupNamingConvention.Factory namingConvention, OsImageToImage osImageToImage,
                               OsImageToHardware osImageToHardware, Map<String, Credentials> credentialStore) {
        this.nodeNamingConvention = checkNotNull(namingConvention, "namingConvention").createWithoutPrefix();
        this.locations = checkNotNull(locations, "locations");
        this.osImageToImage = checkNotNull(osImageToImage, "osImageToImage");
        this.osImageToHardware = checkNotNull(osImageToHardware, "osImageToHardware");
        this.credentialStore = checkNotNull(credentialStore, "credentialStore cannot be null");
    }

    @Override
    public NodeMetadata apply(ServerWithExternalIp from) {
        NodeMetadataBuilder builder = new NodeMetadataBuilder();
        Server server = from.server();
        builder.ids(server.id());
        builder.name(server.name());
        builder.group(nodeNamingConvention.extractGroup(server.name()));

        // TODO
        //builder.hostname(from.getFullyQualifiedDomainName());
        if (server.datacenterId() != null) {
            builder.location(from(locations.get()).firstMatch(
                    LocationPredicates.idEquals(server.datacenterId())).orNull());
        }
//        builder.group(nodeNamingConvention.groupInUniqueNameOrNull(server.name()));
        // TODO
        /*
        builder.hardware(osImageToHardware.apply(from));
        Image image = osImageToImage.apply(from);
        if (image != null) {
            builder.imageId(image.getId());
            builder.operatingSystem(image.getOperatingSystem());
        }
        */

        if (server.state() != null) {
            builder.status(serverStateToNodeStatus.get(server.state()));
        }

        String privateAddress = null;
        if (server.networkInfo() != null && server.networkInfo().primaryNic() != null && server.networkInfo().primaryNic().privateIpv4() != null) {
            privateAddress = server.networkInfo().primaryNic().privateIpv4();
            builder.privateAddresses(ImmutableSet.of(privateAddress));
        }
        // TODO
        if (privateAddress != null && from.externalIp() != null) {
            builder.publicAddresses(ImmutableSet.of(from.externalIp()));
        }

        // DimensionData does not provide a way to get the credentials.
        // Try to return them from the credential store
        Credentials credentials = credentialStore.get("node#" + server.id());
        if (credentials instanceof LoginCredentials) {
            builder.credentials(LoginCredentials.class.cast(credentials));
        }

        return builder.build();
    }
}
