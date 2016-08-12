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
package org.jclouds.dimensiondata.cloudcontroller.compute;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.internal.BaseComputeServiceContextLiveTest;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.dimensiondata.cloudcontroller.DimensionDataCloudControllerApi;
import org.jclouds.dimensiondata.cloudcontroller.compute.options.DimensionDataCloudControllerTemplateOptions;
import org.jclouds.logging.Logger;
import org.jclouds.scriptbuilder.statements.login.AdminAccess;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import javax.inject.Named;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.compute.predicates.NodePredicates.inGroup;
import static org.jclouds.compute.predicates.NodePredicates.runningInGroup;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "DimensionDataCloudControllerComputeServiceContextLiveTest")
public class DimensionDataCloudControllerComputeServiceContextLiveTest extends BaseComputeServiceContextLiveTest {

    private static final int NUM_NODES = 2;

    @Resource
    @Named(ComputeServiceConstants.COMPUTE_LOGGER)
    protected Logger logger = Logger.NULL;

    public DimensionDataCloudControllerComputeServiceContextLiveTest() {
        provider = "dimensiondata-cloudcontroller";
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
        assertThat(view.getComputeService().listImages()).isNotEmpty();
    }

    @Test
    public void testListNodes() throws RunNodesException {
        assertThat(view.getComputeService().listNodes()).isNotNull();
    }

    @Test
    public void testLaunchClusterWithDomainName() throws RunNodesException {
        final String name = "test";

        Template template = view.getComputeService().templateBuilder()
                .osFamily(OsFamily.UBUNTU)
                .locationId("NA9")
                //.minRam(8192)
                //.locationId("NA12")
                .build();

        DimensionDataCloudControllerTemplateOptions options = template.getOptions().as(DimensionDataCloudControllerTemplateOptions.class);
        options
                .inboundPorts(22, 8080, 8081)
                .runScript(AdminAccess.standard())
                .networkDomainId("91f577d2-5812-4e39-a79f-a35e42eb78a6")
                .vlanId("025c59d7-b7e5-4261-95b8-4af067233ee7");

        try {
            Set<? extends NodeMetadata> nodes = view.getComputeService().createNodesInGroup(name, NUM_NODES, template);
            assertThat(nodes).hasSize(NUM_NODES);

            Map<? extends NodeMetadata, ExecResponse> responses = view.getComputeService().runScriptOnNodesMatching(runningInGroup(name), "echo hello");
            assertThat(responses).hasSize(NUM_NODES);

            for (ExecResponse execResponse : responses.values()) {
                assertThat(execResponse.getOutput().trim()).isEqualTo("hello");
            }
        } catch (RunScriptOnNodesException e) {
            Assert.fail();
        } finally {
            view.getComputeService().destroyNodesMatching(inGroup(name));
        }
    }

    @Test
    public void testUnwrapToDimensionDataApi(){
        DimensionDataCloudControllerApi dimensionDataCloudControllerApi =
              view.unwrapApi(DimensionDataCloudControllerApi.class);
        assertNotNull(dimensionDataCloudControllerApi);
    }

    @Test
    public void reconfigureServerCpu(){
        DimensionDataCloudControllerApi dimensionDataCloudControllerApi =
                      view.unwrapApi(DimensionDataCloudControllerApi.class);
        dimensionDataCloudControllerApi.getServerApi()
              // server ID corresponds to "GJ_Test" server in devlab1
              .reconfigureServer("4e00b8f7-28ea-4103-b757-0aa7f3a94e1e", 1, "STANDARD", 1);
    }

    @Override
    protected Iterable<Module> setupModules() {
        return ImmutableSet.<Module>of(getLoggingModule(), new SshjSshClientModule());
    }

}
