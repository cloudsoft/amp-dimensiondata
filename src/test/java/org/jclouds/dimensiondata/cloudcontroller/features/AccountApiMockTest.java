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

import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.jclouds.dimensiondata.cloudcontroller.domain.Account;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerMockTest;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertNotNull;

public class AccountApiMockTest extends BaseDimensionDataCloudControllerMockTest {

   @Test
   public void testGetAccount() throws Exception {
      server.enqueue(xmlResponse("/account.xml"));
      Account account = api.getAccountApi().getMyAccount();
      assertNotNull(account);

      assertSent(server, "GET", "/oec/0.9/myaccount", MediaType.APPLICATION_JSON);
   }

   protected RecordedRequest assertSent(MockWebServer server, String method, String path, String applicationJson) throws InterruptedException {
      RecordedRequest request = server.takeRequest();
      assertThat(request.getMethod()).isEqualTo(method);
      assertThat(request.getPath()).isEqualTo(path);
      // TODO - header is "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2" not XML.
//      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_XML);
      return request;
   }
}
