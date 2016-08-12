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
package org.jclouds.dimensiondata.cloudcontroller.domain;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = "unit", testName = "CpuSpeedTest")
public class CpuSpeedTest {

   public void getDefaultCpuSpeedReturnsStandardSpeed() {
      assertEquals(CpuSpeed.getDefaultCpuSpeed(), CpuSpeed.STANDARD);
   }

   public void getFromJCloudsSpeed() {
      assertEquals(CpuSpeed.fromJCloudsSpeed(10D), CpuSpeed.ECONOMY);
      assertEquals(CpuSpeed.fromJCloudsSpeed(20D), CpuSpeed.STANDARD);
      assertEquals(CpuSpeed.fromJCloudsSpeed(30D), CpuSpeed.HIGHPERFORMANCE);
      assertEquals(CpuSpeed.fromJCloudsSpeed(666D), CpuSpeed.getDefaultCpuSpeed());
   }

   public void getFromDimensionDataSpeed() {
      assertEquals(CpuSpeed.fromDimensionDataSpeed("ECONOMY"), CpuSpeed.ECONOMY);
      assertEquals(CpuSpeed.fromDimensionDataSpeed("STANDARD"), CpuSpeed.STANDARD);
      assertEquals(CpuSpeed.fromDimensionDataSpeed("HIGHPERFORMANCE"), CpuSpeed.HIGHPERFORMANCE);
      assertEquals(CpuSpeed.fromDimensionDataSpeed("INVALID"), CpuSpeed.getDefaultCpuSpeed());
   }
}
