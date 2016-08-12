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

import javax.inject.Singleton;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.compute.strategy.CreateNodesInGroupThenAddToSet;
import org.jclouds.compute.strategy.PrioritizeCredentialsFromTemplate;
import org.jclouds.dimensiondata.cloudcontroller.compute.DimensionDataCloudControllerComputeService;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.DatacenterToLocation;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.OsImageToHardware;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.OsImageToImage;
import org.jclouds.dimensiondata.cloudcontroller.compute.functions.ServerWithNatRuleToNodeMetadata;
import org.jclouds.dimensiondata.cloudcontroller.compute.options.DimensionDataCloudControllerTemplateOptions;
import org.jclouds.dimensiondata.cloudcontroller.compute.DimensionDataCloudControllerComputeServiceAdapter;
import org.jclouds.dimensiondata.cloudcontroller.compute.strategy.GetOrCreateNetworkDomainThenCreateNodes;
import org.jclouds.dimensiondata.cloudcontroller.compute.strategy.UseNodeCredentialsButOverrideFromTemplate;
import org.jclouds.dimensiondata.cloudcontroller.domain.Datacenter;
import org.jclouds.dimensiondata.cloudcontroller.domain.OsImage;
import org.jclouds.dimensiondata.cloudcontroller.domain.internal.ServerWithExternalIp;
import org.jclouds.domain.Location;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

public class DimensionDataCloudControllerComputeServiceContextModule extends
        ComputeServiceAdapterContextModule<ServerWithExternalIp, OsImage, OsImage, Datacenter> {

    private DimensionDataCloudControllerTemplateOptions templateOptions;

    @Override
    protected void configure() {
        super.configure();
        bind(new TypeLiteral<ComputeServiceAdapter<ServerWithExternalIp, OsImage, OsImage, Datacenter>>() {
        }).to(DimensionDataCloudControllerComputeServiceAdapter.class);
        bind(ComputeService.class).to(DimensionDataCloudControllerComputeService.class);

        bind(new TypeLiteral<Function<ServerWithExternalIp, NodeMetadata>>() {
        }).to(ServerWithNatRuleToNodeMetadata.class);
        bind(new TypeLiteral<Function<OsImage, Image>>() {
        }).to(OsImageToImage.class);
        bind(new TypeLiteral<Function<OsImage, Hardware>>() {
        }).to(OsImageToHardware.class);
        bind(new TypeLiteral<Function<Datacenter, Location>>() {
        }).to(DatacenterToLocation.class);
        bind(TemplateOptions.class).to(DimensionDataCloudControllerTemplateOptions.class);
        bind(CreateNodesInGroupThenAddToSet.class).to(GetOrCreateNetworkDomainThenCreateNodes.class);
        bind(PrioritizeCredentialsFromTemplate.class).to(UseNodeCredentialsButOverrideFromTemplate.class);

        // to have the compute service adapter override default locations
        install(new LocationsFromComputeServiceAdapterModule<ServerWithExternalIp, OsImage, OsImage, Datacenter>() {
        });

    }

    @Override
    protected TemplateOptions provideTemplateOptions(Injector injector, TemplateOptions options) {
        this.templateOptions = (DimensionDataCloudControllerTemplateOptions) options;
        return super.provideTemplateOptions(injector, options);
    }

    @Provides
    @Singleton
    protected Supplier<DimensionDataCloudControllerTemplateOptions> getTemplateOptions() {
        return new Supplier<DimensionDataCloudControllerTemplateOptions>() {
            @Override
            public DimensionDataCloudControllerTemplateOptions get() {
                return templateOptions;
            }
        };
    }
}
