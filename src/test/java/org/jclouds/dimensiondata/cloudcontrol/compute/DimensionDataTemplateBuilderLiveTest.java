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
package org.jclouds.dimensiondata.cloudcontrol.compute;

import java.util.Set;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.compute.internal.BaseTemplateBuilderLiveTest;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApiMetadata;
import org.testng.annotations.Test;

@Test(groups = "live", testName = "DimensionDataTemplateBuilderLiveTest")
public class DimensionDataTemplateBuilderLiveTest extends BaseTemplateBuilderLiveTest {

    private DimensionDataCloudControlApiMetadata apiMetadata;

    public DimensionDataTemplateBuilderLiveTest() {
        super();

        provider = "dimensiondata-cloudcontrol";
    }

    @Override
    protected ApiMetadata createApiMetadata() {
        if (apiMetadata == null) {
            apiMetadata = new DimensionDataCloudControlApiMetadata();
        }
        return apiMetadata;
    }

    @Override
    protected Set<String> getIso3166Codes() {
        return createProviderMetadata().getIso3166Codes();
    }

}
