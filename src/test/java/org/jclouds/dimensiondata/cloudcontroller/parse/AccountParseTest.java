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
package org.jclouds.dimensiondata.cloudcontroller.parse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jclouds.dimensiondata.cloudcontroller.domain.Account;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerParseTest;
import org.jclouds.dimensiondata.cloudcontroller.parsers.ParseAccount;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.config.SaxParserModule;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;


import static org.testng.Assert.assertEquals;

@Test(groups = "unit")
public class AccountParseTest extends BaseDimensionDataCloudControllerParseTest<Account> {

       protected ParseSax.Factory factory;
       private Injector injector;

       @Override
       public void test() {
          InputStream is = getClass().getResourceAsStream("/account.xml");
          injector = Guice.createInjector(new SaxParserModule());
          factory = injector.getInstance(ParseSax.Factory.class);
          Account result = factory.create(new ParseAccount()).parse(is);
          assertEquals(result, expected());
       }

       @Override
       @Consumes(MediaType.APPLICATION_XML)
       public Account expected()
       {
           return Account.builder().userName("devlab1-online-vendor-devuser1").fullName("Donal Lunny").firstName("Donal")
                   .lastName("Lunny").emailAddress("devlab1-online-vendor-devuser1@opsource.net")
                   .orgId("6ac1e746-b1ea-4da5-a24e-caf1a978789d")
                   .build();
       }

}
