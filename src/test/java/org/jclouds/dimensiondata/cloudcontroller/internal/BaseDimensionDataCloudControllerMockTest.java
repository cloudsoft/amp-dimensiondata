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
package org.jclouds.dimensiondata.cloudcontroller.internal;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.dimensiondata.cloudcontroller.DimensionDataCloudControllerApi;
import org.jclouds.dimensiondata.cloudcontroller.DimensionDataCloudControllerProviderMetadata;
import org.jclouds.json.Json;
import org.jclouds.rest.ApiContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import com.google.gson.JsonParser;
import com.google.inject.Module;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

/**
 * Base class for all DimensionDataCloudController mock tests.
 */
public class BaseDimensionDataCloudControllerMockTest {

   private static final String DEFAULT_ENDPOINT = new DimensionDataCloudControllerProviderMetadata().getEndpoint();

   /*extends BaseMockWebServerTest {

   @Override
   protected void addOverrideProperties(Properties properties) {
      properties.setProperty(PROPERTY_IDENTITY, "username");
      properties.setProperty(PROPERTY_CREDENTIAL, "password");
   }

   @Override
   protected Module createConnectionModule() {
      return new OkHttpCommandExecutorServiceModule();
   }

   public byte[] payloadFromResource(String resource) {
      try {
         return toStringAndClose(getClass().getResourceAsStream(resource)).getBytes(Charsets.UTF_8);
      } catch (IOException e) {
         throw Throwables.propagate(e);
      }
   }

   protected RecordedRequest assertSent(MockWebServer server, String method, String path) throws InterruptedException {
      RecordedRequest request = server.takeRequest();
      assertThat(request.getMethod()).isEqualTo(method);
      assertThat(request.getPath()).isEqualTo(path);
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON);
      return request;
   }
*/
private final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(sameThreadExecutor()));

   protected MockWebServer server;
   protected DimensionDataCloudControllerApi api;
   private Json json;

   // So that we can ignore formatting.
   private final JsonParser parser = new JsonParser();

   @BeforeMethod
   public void start() throws IOException {
      server = new MockWebServer();
      server.play();
      ApiContext<DimensionDataCloudControllerApi> ctx = ContextBuilder.newBuilder("dimensiondata-cloudcontroller")
              .credentials("", "")
              .endpoint(url(""))
              //.modules(modules)
              .overrides(overrides())
              .build();
      json = ctx.utils().injector().getInstance(Json.class);
      api = ctx.getApi();
      server.enqueue(xmlResponse("/account.xml"));
   }

   @AfterMethod(alwaysRun = true)
   public void stop() throws IOException {
      server.shutdown();
      api.close();
   }

   protected Properties overrides() {
      return new Properties();
   }

   protected String url(String path) {
      return server.getUrl(path).toString();
   }

   protected MockResponse jsonResponse(String resource) {
      return new MockResponse().addHeader("Content-Type", "application/json").setBody(stringFromResource(resource));
   }

   protected MockResponse xmlResponse(String resource) {
      return new MockResponse().addHeader("Content-Type", "application/xml").setBody(stringFromResource(resource));
   }

   protected MockResponse responseUnexpectedError() {
      return new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("content: [{\"operation\":\"OPERATION\",\"responseCode\":\"UNEXPECTED_ERROR\"}]");
   }

   protected MockResponse responseResourceNotFound() {
      return new MockResponse().setResponseCode(400).setStatus("HTTP/1.1 400 Bad Request").setBody("content: [{\"operation\":\"OPERATION\",\"responseCode\":\"RESOURCE_NOT_FOUND\"}]");
   }


   protected MockResponse response200() {
      return new MockResponse().setResponseCode(200);
   }

   protected String stringFromResource(String resourceName) {
      try {
         return Resources.toString(getClass().getResource(resourceName), Charsets.UTF_8)
                 .replace(DEFAULT_ENDPOINT, url(""));
      } catch (IOException e) {
         throw Throwables.propagate(e);
      }
   }

   protected RecordedRequest assertSent(MockWebServer server, String method, String path) throws InterruptedException {
      RecordedRequest request = server.takeRequest();
      assertThat(request.getMethod()).isEqualTo(method);
      assertThat(request.getPath()).isEqualTo(path);
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON);
      return request;
   }

}
