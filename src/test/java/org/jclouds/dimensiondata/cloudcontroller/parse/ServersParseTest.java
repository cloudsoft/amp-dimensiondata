/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.dimensiondata.cloudcontroller.parse;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.jclouds.dimensiondata.cloudcontroller.domain.CPU;
import org.jclouds.dimensiondata.cloudcontroller.domain.Disk;
import org.jclouds.dimensiondata.cloudcontroller.domain.NIC;
import org.jclouds.dimensiondata.cloudcontroller.domain.OperatingSystem;
import org.jclouds.dimensiondata.cloudcontroller.domain.Server;
import org.jclouds.dimensiondata.cloudcontroller.domain.Servers;
import org.jclouds.dimensiondata.cloudcontroller.domain.VMwareTools;
import org.jclouds.dimensiondata.cloudcontroller.domain.VirtualHardware;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerParseTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

import autovalue.shaded.com.google.common.common.collect.Lists;


@Test(groups = "unit")
public class ServersParseTest extends BaseDimensionDataCloudControllerParseTest<Servers> {

    @Override
    public String resource() {
        return "/servers.json";
    }

    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    public Servers expected() {
        List<Server> servers = ImmutableList.of(
                Server.builder()
                        .id("b8246ba4-847d-475b-b296-f76787a69ca8")
                        .name("gui")
                        .description("")
                        .operatingSystem(OperatingSystem.create(
                                "REDHAT664",
                                "REDHAT6/64",
                                "UNIX"
                        ))
                        .cpu(CPU.create(
                                2,
                                "STANDARD",
                                1
                        ))
                        .memoryGb(4)
                        .disks(ImmutableList.of(
                                Disk.builder()
                                        .id("0ba67812-d7b7-4c3f-b114-870fbea24d42")
                                        .scsiId(0)
                                        .sizeGb(10)
                                        .speed("STANDARD")
                                        .state("NORMAL")
                                        .build()
                                )
                        )
                        .nic(NIC.builder()
                                .id("7c5310cd-ec36-4f76-9cc5-046f197360e3")
                                .privateIpv4("10.230.59.11")
                                .networkId("c5f083a2-2144-11e4-b29c-001517c4643e")
                                .networkName("A Network Domain")
                                .state("NORMAL")
                                .build())
                        .softwareLabels(Lists.newArrayList())
                        .sourceImageId("1e44ab3f-2426-45ec-a1b5-827b2ce58836")
                        .createTime("2016-03-10T13:05:21.000Z")
                        .deployed(true)
                        .started(true)
                        .state("NORMAL")
                        .vmwareTools(VMwareTools.create(
                                "NEED_UPGRADE",
                                "RUNNING",
                                9354
                        ))
                        .virtualHardware(VirtualHardware.create(
                                "vmx-08",
                                false
                        ))
                        .datacenterId("NA3")
                        .build()
        );
        return new Servers(servers, 1, 5, 5, 250);
    }
}