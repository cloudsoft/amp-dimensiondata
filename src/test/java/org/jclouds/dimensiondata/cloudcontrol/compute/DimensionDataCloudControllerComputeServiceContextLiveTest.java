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
package org.jclouds.dimensiondata.cloudcontrol.compute;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.compute.predicates.NodePredicates.inGroup;

import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Named;

import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.internal.BaseComputeServiceContextLiveTest;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.dimensiondata.cloudcontrol.compute.options.DimensionDataCloudControllerTemplateOptions;
import org.jclouds.logging.Logger;
import org.jclouds.scriptbuilder.statements.login.AdminAccess;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

@Test(groups = "live", testName = "DimensionDataCloudControllerComputeServiceContextLiveTest")
public class DimensionDataCloudControllerComputeServiceContextLiveTest extends BaseComputeServiceContextLiveTest {

    private static final int NUM_NODES = 2;

    @Resource
    @Named(ComputeServiceConstants.COMPUTE_LOGGER)
    protected Logger logger = Logger.NULL;

    public DimensionDataCloudControllerComputeServiceContextLiveTest() {
        provider = "dimensiondata-cloudcontrol";
    }

    @Test
    public void testListHardwareProfiles() {
        Set<? extends Hardware> hardwareProfiles = view.getComputeService().listHardwareProfiles();
        assertThat(hardwareProfiles).isNotEmpty();
        for (Hardware hardwareProfile : hardwareProfiles) {
            System.out.println(hardwareProfile);
        }
    }

    @Test
    public void testListAvailableLocations() throws RunNodesException {
        assertThat(view.getComputeService().listAssignableLocations()).isNotEmpty();
    }

    @Test
    public void testListImages() throws RunNodesException {
        Set<? extends Image> images = view.getComputeService().listImages();
        assertThat(images).isNotEmpty();
        for (Image image : images) {
            System.out.println(image);
        }
    }

    @Test
    public void testListNodes() throws RunNodesException {
        assertThat(view.getComputeService().listNodes()).isNotNull();
    }

    @Test
    public void testLaunchClusterWithDomainName() throws RunNodesException {
        final String name = "test";

        Template template = view.getComputeService().templateBuilder()
//                .osFamily(OsFamily.UBUNTU)
                .imageId("99cc6f2f-6e4f-4e67-b5fc-b702b613efb0") // centos7 EU7
//                .imageId("4197a614-35c6-47e4-8468-80ef850d7820") // ubuntu 16.04.2 EU7
                .locationId("EU7")
                //.minRam(8192)
                //.locationId("NA12")
                .build();

        DimensionDataCloudControllerTemplateOptions options = template.getOptions().as(DimensionDataCloudControllerTemplateOptions.class);
        options
                .inboundPorts(22, 8080, 8081)
                .runScript(AdminAccess.standard())
                .networkDomainId("e0f3ccc8-9214-4fe2-8063-4f5ad559afdb")
                .vlanId("2928c6f2-574b-4b25-ac8e-3fea7d9f344c");

        try {
            Set<? extends NodeMetadata> nodes = view.getComputeService().createNodesInGroup(name, NUM_NODES, template);
            assertThat(nodes).hasSize(NUM_NODES);

//            Map<? extends NodeMetadata, ExecResponse> responses = view.getComputeService().runScriptOnNodesMatching(runningInGroup(name), "echo hello");
//            assertThat(responses).hasSize(NUM_NODES);
//
//            for (ExecResponse execResponse : responses.values()) {
//                assertThat(execResponse.getOutput().trim()).isEqualTo("hello");
//            }
//        } catch (RunScriptOnNodesException e) {
//            Assert.fail();
        } finally {
            view.getComputeService().destroyNodesMatching(inGroup(name));
        }
}

    @Override
    protected Iterable<Module> setupModules() {
        return ImmutableSet.<Module>of(getLoggingModule(), new SshjSshClientModule());
    }

}
