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

import com.google.common.collect.Lists;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.jclouds.dimensiondata.cloudcontroller.domain.CPU;
import org.jclouds.dimensiondata.cloudcontroller.domain.Disk;
import org.jclouds.dimensiondata.cloudcontroller.domain.NIC;
import org.jclouds.dimensiondata.cloudcontroller.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.options.CreateServerOptions;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerMockTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Mock tests for the {@link ServerApi} class.
 */
@Test(groups = "unit", testName = "ServerApiMockTest", singleThreaded = true)
public class ServerApiMockTest extends BaseDimensionDataCloudControllerMockTest {

   public void testDeployServerReturnsUnexpectedError() throws InterruptedException {
      server.enqueue(responseUnexpectedError());
      server.enqueue(response200());
      NetworkInfo networkInfo = NetworkInfo.create(
            "networkDomainId",
            NIC.builder().vlanId("vlanId").build(),
            // TODO allow additional NICs
            Lists.<NIC>newArrayList()
      );
      Response response = api.getServerApi().deployServer(ServerApiMockTest.class.getSimpleName(),
            "imageId",
            true,
            networkInfo,
            Lists.<Disk>newArrayList(),
            "administratorPassword");

      assertNull(response);

      assertEquals(server.getRequestCount(), 2);
      assertSent(server, "POST", "/caas/2.2/" + ORG_ID + "/server/deployServer");
   }

   public void testDeployServerWithSpecificCpu() throws InterruptedException {
      server.enqueue(response200());

      NetworkInfo networkInfo = NetworkInfo.create(
            "networkDomainId",
            NIC.builder().vlanId("vlanId").build(),
            Lists.<NIC>newArrayList()
      );

      CreateServerOptions createServerOptions = CreateServerOptions.builder()
            .cpu(CPU.builder()
                  .count(1)
                  .speed("HIGHPERFORMANCE")
                  .coresPerSocket(2)
                  .build())
            .build();
      api.getServerApi().deployServer(
            ServerApiMockTest.class.getSimpleName(),
            "imageId",
            true,
            networkInfo,
            Lists.<Disk>newArrayList(),
            "administratorPassword",
            createServerOptions);
      RecordedRequest recordedRequest = assertSent(server, "POST", "/caas/2.2/" + ORG_ID + "/server/deployServer");
      assertBodyContains(
            recordedRequest, "\"cpu\":{\"count\":1,\"speed\":\"HIGHPERFORMANCE\",\"coresPerSocket\":2}");
   }

   public void testReconfigureCpu() throws Exception {
      server.enqueue(response200());

      api.getServerApi().reconfigureServer("serverId", 2, "ECONOMY", 4);

      RecordedRequest recordedRequest = assertSent(server, "POST", "/caas/2.2/" + ORG_ID + "/server/reconfigureServer");
      assertBodyContains(recordedRequest,
            "{\"id\":\"serverId\",\"cpuCount\":2,\"cpuSpeed\":\"ECONOMY\",\"coresPerSocket\":4}");
   }

/*
    public void testGetServerReturnsResourceNotFound() throws InterruptedException {
        server.enqueue(responseResourceNotFound());
        server.enqueue(response200());

        Server found = api.getServerApi().getServer("12345");

        assertNull(found);

        assertEquals(server.getRequestCount(), 2);
        assertSent(server, "POST", "/server/server/12345");
    }

    public void testListServers() throws Exception {
        MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/servers.json")));
        ServerApi api = api(server);

        try {
            List<Server> servers = api.listServers().concat().toList();

            assertSent(server, "GET", "/server/server");
            assertEquals(servers.size(), 1);
            for (Server s : servers) {
                assertNotNull(s);
            }

        } finally {
            server.shutdown();
        }
    }

    public void testGetServer() throws Exception {
        MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/server.json")));
        ServerApi api = api(server);

        try {
            Server found = api.getServer("12345");

            assertSent(server, "GET", "/server/server/12345");
            assertNotNull(found);

        } finally {
            server.shutdown();
        }
    }

    public void testDeleteServer() throws Exception {
        MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/deleteServer.json")));
        ServerApi api = api(server);

        try {
            Response found = api.deleteServer("12345");

            assertSent(server, "POST", "/server/deleteServer");
            assertNotNull(found);

        } finally {
            server.shutdown();
        }
    }
*/

}
