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

import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerApiLiveTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "ServerCloneApiLiveTest", singleThreaded = true)
public class ServerCloneApiLiveTest extends BaseDimensionDataCloudControllerApiLiveTest
{
    @Test
    public void testCloneServer()
    {
        Response response = api().clone("0896551e-4fe3-4450-a627-ad5548e7e83a", "trevor-test2", "trevor-description2");
        assertNotNull(response);
        if (null != response)
        {
            response.info();
        }
    }

    private ServerCloneApi api()
    {
        return api.getServerCloneApi();
    }

}
