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
package org.jclouds.dimensiondata.cloudcontroller.compute.config;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.DatacenterToLocation;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.OperatingSystemToImage;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.ServerToNodeMetadata;
import org.jclouds.dimensiondata.cloudcontroller.compute.options.DimensionDataCloudControllerTemplateOptions;
import org.jclouds.dimensiondata.cloudcontroller.compute.strategy.DimensionDataCloudControllerComputeServiceAdapter;
import org.jclouds.dimensiondata.cloudcontroller.domain.Datacenter;
import org.jclouds.dimensiondata.cloudcontroller.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontroller.domain.Server;
import org.jclouds.domain.Location;
import org.jclouds.functions.IdentityFunction;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

public class DimensionDataCloudControllerComputeServiceContextModule extends
        ComputeServiceAdapterContextModule<Server, Hardware, OperatingSystem, Datacenter> {

    @Override
    protected void configure() {
        super.configure();
        bind(new TypeLiteral<ComputeServiceAdapter<Server, Hardware, OperatingSystem, Datacenter>>() {
        }).to(DimensionDataCloudControllerComputeServiceAdapter.class);
        bind(new TypeLiteral<Function<Server, NodeMetadata>>() {
        }).to(ServerToNodeMetadata.class);
        bind(new TypeLiteral<Function<OperatingSystem, Image>>() {
        }).to(OperatingSystemToImage.class);
        bind(new TypeLiteral<Function<Hardware, Hardware>>() {
        }).to(Class.class.cast(IdentityFunction.class));
        // TODO we aren't converting yet hardware from a provider-specific type
        bind(new TypeLiteral<Function<Hardware, Hardware>>() {
        }).to(Class.class.cast(IdentityFunction.class));
        bind(new TypeLiteral<Function<Datacenter, Location>>() {
        }).to(DatacenterToLocation.class);
        bind(TemplateOptions.class).to(DimensionDataCloudControllerTemplateOptions.class);
        // to have the compute service adapter override default locations
        install(new LocationsFromComputeServiceAdapterModule<Server, Hardware, OperatingSystem, Datacenter>() {
        });

    }
}