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

import java.util.Properties;

import org.jclouds.apis.BaseApiLiveTest;
import org.jclouds.dimensiondata.cloudcontroller.DimensionDataCloudControllerApi;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

@Test(groups = "live")
public class BaseDimensionDataCloudControllerApiLiveTest extends BaseApiLiveTest<DimensionDataCloudControllerApi> {

    protected String ORG_ID = System.getProperty("test.dimensiondata-cloudcontroller.orgId", "250c2c09-ffed-4b44-8b80-3000b6088074");

    public BaseDimensionDataCloudControllerApiLiveTest() {
        provider = "dimensiondata-cloudcontroller";
    }

    @Override
    protected Iterable<Module> setupModules() {
        return ImmutableSet.<Module>of(getLoggingModule(), new SshjSshClientModule());
    }

    @Override
    protected Properties setupProperties() {
        Properties overrides = super.setupProperties();
        overrides.setProperty("jclouds.ssh.retry-auth", "true");
        // Uncomment the following lines to configure jclouds to use a proxy server
        // This is useful for debugging with a proxy server such as Charles
        // overrides.setProperty(Constants.PROPERTY_PROXY_HOST, "localhost");
        // overrides.setProperty(Constants.PROPERTY_PROXY_PORT, "8888");
        return overrides;
    }

}
