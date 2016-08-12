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
package org.jclouds.dimensiondata.cloudcontroller.filters;

import org.jclouds.dimensiondata.cloudcontroller.compute.DimensionDataCloudControllerComputeServiceAdapter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = "unit", testName = "OrganisationIdFilterTest")
public class OrganisationIdFilterTest {

   @BeforeClass
   public void setup(){
      DimensionDataCloudControllerComputeServiceAdapter.ORG_ID = "ORG-ID";
   }

   @Test
   public void testCaasUrl(){
      String expectedPath = "https://apidevlab1.opsourcecloud.net/caas/2.2/ORG-ID/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2";
      String updatedPath = new OrganisationIdFilter().injectOrganisationId(
            "https://apidevlab1.opsourcecloud.net/caas/2.2/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2");
      assertEquals(expectedPath, updatedPath);
   }
   @Test
   public void testOecUrl(){
      String expectedPath = "https://apidevlab1.opsourcecloud.net/oec/0.9/ORG-ID/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2";
      String updatedPath = new OrganisationIdFilter().injectOrganisationId(
            "https://apidevlab1.opsourcecloud.net/oec/0.9/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2");
      assertEquals(expectedPath, updatedPath);
   }

}
