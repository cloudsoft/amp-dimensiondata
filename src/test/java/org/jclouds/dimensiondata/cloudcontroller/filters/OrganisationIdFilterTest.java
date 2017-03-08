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

import com.google.common.base.Supplier;
import org.jclouds.dimensiondata.cloudcontroller.compute.suppliers.OrganisationIdForAccount;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerMockTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = "unit", testName = "OrganisationIdFilterTest")
public class OrganisationIdFilterTest extends BaseDimensionDataCloudControllerMockTest {

   @Test
   public void testCaasUrl() {
      Supplier<String> orgIdSupplier = new OrganisationIdForAccount(api);
      String expectedPath = "/caas/2.2/6ac1e746-b1ea-4da5-a24e-caf1a978789d/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2";
      String updatedPath = new OrganisationIdFilter(orgIdSupplier).injectOrganisationId(
            "/caas/2.2/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2");
      assertEquals(updatedPath, expectedPath);
   }

   @Test
   public void testOecUrl() {
      Supplier<String> orgIdSupplier = new OrganisationIdForAccount(api);
      String expectedPath = "/oec/0.9/6ac1e746-b1ea-4da5-a24e-caf1a978789d/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2";
      String updatedPath = new OrganisationIdFilter(orgIdSupplier).injectOrganisationId(
            "/oec/0.9/server/0896551e-4fe3-4450-a627-ad5548e7e83a?clone=trevor-test2&desc=trevor-description2");
      assertEquals(updatedPath, expectedPath);
   }

}
