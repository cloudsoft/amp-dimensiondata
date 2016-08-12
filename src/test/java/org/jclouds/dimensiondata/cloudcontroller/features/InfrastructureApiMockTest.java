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

import static com.google.common.collect.Iterables.size;
import static org.testng.Assert.assertEquals;

import org.jclouds.dimensiondata.cloudcontroller.domain.Datacenter;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerMockTest;
import org.testng.annotations.Test;

/**
 * Mock tests for the {@link InfrastructureApi} class.
 */
@Test(groups = "unit", testName = "InfrastructureApiMockTest")
public class InfrastructureApiMockTest extends BaseDimensionDataCloudControllerMockTest {


    public void testListDatacenters() throws Exception {
        server.enqueue(jsonResponse("/datacenters.json"));
        Iterable<Datacenter> datacenters = api.getInfrastructureApi().listDatacenters().concat();

        assertEquals(size(datacenters), 2); // Force the PagedIterable to advance
        assertEquals(server.getRequestCount(), 1);


        assertSent(server, "GET", "/caas/2.2/" + ORG_ID + "/infrastructure/datacenter");
    }

}
