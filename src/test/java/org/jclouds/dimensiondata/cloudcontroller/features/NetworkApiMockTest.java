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

import org.jclouds.dimensiondata.cloudcontroller.domain.NetworkDomain;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerMockTest;
import org.testng.annotations.Test;

/**
 * Mock tests for the {@link org.jclouds.dimensiondata.cloudcontroller.features.NetworkApi} class.
 */
@Test(groups = "unit", testName = "NetworkApiMockTest")
public class NetworkApiMockTest extends BaseDimensionDataCloudControllerMockTest {

    public void testListNetworkDomains() throws Exception {
        server.enqueue(jsonResponse("/networkDomains.json"));
        //MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/networkDomains.json")));
        //NetworkApi api = api(server);
        Iterable<NetworkDomain> networkDomains = api.getNetworkApi().listNetworkDomains().concat();

        assertEquals(size(networkDomains), 1); // Force the PagedIterable to advance
        assertEquals(server.getRequestCount(), 1);

        assertSent(server, "GET", "/caas/2.2/" + ORG_ID + "/network/networkDomain");
    }
/*
    public void testListVlans() throws Exception {
        MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/vlans.json")));
        NetworkApi api = api(server);

        try {
            assertEquals(api.listVlans("12345").concat().toList(), new VlansParseTest().expected().toList());
            assertSent(server, "GET", "/network/vlan?networkDomainId=12345");
        } finally {
            server.shutdown();
        }
    }

    public void testListPublicIPv4AddressBlock() throws Exception {
        MockWebServer server = mockWebServer(new MockResponse().setBody(payloadFromResource("/publicIpBlocks.json")));
        NetworkApi api = api(server);

        try {
            assertEquals(api.listPublicIPv4AddressBlocks("12345").concat().toList(), new PublicIpBlocksParseTest().expected().toList());
            assertSent(server, "GET", "/network/publicIpBlock?networkDomainId=12345");
        } finally {
            server.shutdown();
        }
    }

    public void testCreateFirewallRule() throws Exception {
        MockWebServer server = mockWebServer(
                new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("{ \"responseCode\":\"RETRYABLE_SYSTEM_ERROR\",\"message\":\"Please try again later.\" }"),
                new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("{ \"responseCode\":\"RETRYABLE_SYSTEM_ERROR\",\"message\":\"Please try again later.\" }"),
                new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("{ \"responseCode\":\"RETRYABLE_SYSTEM_ERROR\",\"message\":\"Please try again later.\" }"),
                new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("content: [{\"operation\":\"CREATE_FIREWALL_RULE\",\"responseCode\":\"RESOURCE_BUSY\",\"message\":\"Another Firewall Rule operation is in progress for Network Domain with Id 12b1fa4c-a2fd-4a84-84e0-f4c80d91e2ea. Please try again later.\",\"info\":[],\"warning\":[],\"error\":[],\"requestId\":\"na/2016-04-04T03:55:35.057-04:00/be8c37c9-46fa-4a73-9553-38c1bb7c291b\"}]"),
                new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("{ \"responseCode\":\"RETRYABLE_SYSTEM_ERROR\",\"message\":\"Please try again later.\" }"),
                new MockResponse().setResponseCode(200).setBody("{\n" +
                        "\"operation\": \"CREATE_FIREWALL_RULE\",\n" +
                        "\"responseCode\": \"OK\",\n" +
                        "\"message\": \"Request create Firewall Rule 'My.Rule' successful\", \"info\": [\n" +
                        "{\n" +
                        "\"name\": \"firewallRuleId\",\n" +
                        "\"value\": \"dc545f3e-823c-4500-93c9-8d7f576311de\"\n" +
                        "} ],\n" +
                        "\"warning\": [],\n" +
                        "\"error\": [],\n" +
                        "\"requestId\": \"NA9/2015-03-05T13:46:34.848-05:00/f8fdef24-8a12-45ea-a831-\n" +
                        "d5463212ef6a\" }")
        );
        NetworkApi api = api(server);
        try {
            api.createFirewallRule("123456", "test", DEFAULT_ACTION, DEFAULT_IP_VERSION, DEFAULT_PROTOCOL,
                    FirewallRuleTarget.builder()
                            .ip(IpRange.create("ANY", null))
                            .build(),
                    FirewallRuleTarget.builder()
                            .ip(IpRange.create("ANY", null))
                            .build(),
                    true,
                    Placement.builder().position("LAST").build());
            assertSent(server, "POST", "/network/createFirewallRule");
        } finally {
            server.shutdown();
        }
    }

    private NetworkApi api(MockWebServer server) {
        return api(DimensionDataCloudControllerApi.class,
                server.getUrl("/").toString(),
                //new DimensionDataCloudControllerComputeServiceContextModule(),
                new DimensionDataCloudControllerHttpApiModule(),
                new SLF4JLoggingModule(),
                new OkHttpCommandExecutorServiceModule()
        ).getNetworkApi();
    }
*/
}
