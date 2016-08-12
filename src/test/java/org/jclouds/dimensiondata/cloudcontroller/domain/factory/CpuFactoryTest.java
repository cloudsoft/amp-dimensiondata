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
package org.jclouds.dimensiondata.cloudcontroller.domain.factory;

import org.jclouds.compute.domain.Processor;
import org.jclouds.dimensiondata.cloudcontroller.domain.CPU;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

@Test(groups = "unit", testName = "CpuFactoryTest")
public class CpuFactoryTest {

   private static final double ECONOMY_SPEED = 10D;
   private static final double HIGHPERFORMANCE_SPEED = 30D;
   private static final double INVALID_SPEED = 666D;
   private static final double TWO_CORES = 2D;
   private static final double FOUR_CORES = 4D;

   private CpuFactory factory = new CpuFactory();

   public void singleProcessor() {
      final CPU cpu = factory.create(Collections.singletonList(new Processor(TWO_CORES, ECONOMY_SPEED)));
      assertEquals((int) cpu.count(), 1);
      assertEquals(cpu.speed(), "ECONOMY");
      assertEquals((int) cpu.coresPerSocket(), 2);
   }

   public void multipleIdenticalProcessors() {
      final Processor processor1 = new Processor(TWO_CORES, ECONOMY_SPEED);
      final Processor processor2 = new Processor(TWO_CORES, ECONOMY_SPEED);
      List<Processor> processorList = new ArrayList<Processor>();
      processorList.add(processor1);
      processorList.add(processor2);

      final CPU cpu = factory.create(processorList);
      assertEquals((int) cpu.count(), 2);
      assertEquals(cpu.speed(), "ECONOMY");
      assertEquals((int) cpu.coresPerSocket(), 2);
   }

   public void inconsistentProcessorSpeedsMeansDefaultSpeedUsed() {
      final Processor processor1 = new Processor(TWO_CORES, ECONOMY_SPEED);
      final Processor processor2 = new Processor(TWO_CORES, HIGHPERFORMANCE_SPEED);
      List<Processor> processorList = new ArrayList<Processor>();
      processorList.add(processor1);
      processorList.add(processor2);

      final CPU cpu = factory.create(processorList);
      assertEquals((int) cpu.count(), 2);
      assertEquals(cpu.speed(), "STANDARD");
      assertEquals((int) cpu.coresPerSocket(), 2);
   }

   public void inconsistentProcessorCoresMeansDefaultCoresUsed() {
      final Processor processor1 = new Processor(TWO_CORES, ECONOMY_SPEED);
      final Processor processor2 = new Processor(FOUR_CORES, ECONOMY_SPEED);
      List<Processor> processorList = new ArrayList<Processor>();
      processorList.add(processor1);
      processorList.add(processor2);

      final CPU cpu = factory.create(processorList);
      assertEquals((int) cpu.count(), 2);
      assertEquals(cpu.speed(), "ECONOMY");
      assertEquals((int) cpu.coresPerSocket(), 1);
   }

   public void invalidProcessorSpeedMeansDefaultSpeedUsed() {
      final CPU cpu = factory.create(Collections.singletonList(new Processor(TWO_CORES, INVALID_SPEED)));
      assertEquals((int) cpu.count(), 1);
      assertEquals(cpu.speed(), "STANDARD");
      assertEquals((int) cpu.coresPerSocket(), 2);
   }

   public void emptyProcessorListMeansDefaultCpuUsed() {
      final CPU cpu = factory.create(Collections.<Processor>emptyList());
      assertEquals((int) cpu.count(), 1);
      assertEquals(cpu.speed(), "STANDARD");
      assertEquals((int) cpu.coresPerSocket(), 1);
   }

}
