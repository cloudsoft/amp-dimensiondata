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
import org.jclouds.dimensiondata.cloudcontroller.domain.CpuSpeed;

import java.util.List;

public class CpuFactory {

   /**
    * Creates a CPU instance for the supplied processor list.
    * <p/>
    * The {@link CpuSpeed#getDefaultCpuSpeed()} speed will be selected by default if invalid input is supplied, eg:
    * <ul>
    * <li>Differing speed values across processors - this is not valid in the Dimension Data cloud.</li>
    * <li>Invalid speed value found that is not mapped in {@link CpuSpeed}.</li>
    * </ul>
    * <p/>
    * The value of coresPerSocket will be set to 1 if invalid input is supplied, eg:
    * <ul>
    * <li>Differing cores values across processors - this is not valid in the Dimension Data cloud.</li>
    * </ul>
    * <p/>
    * If the processor list is empty, then a {@link CpuSpeed#getDefaultCpuSpeed()} speed CPU with 1 core is assumed.
    *
    * @param processorList the input processor list - may be empty.
    * @return CPU instance representing the supplied processor list.
    */
   public CPU create(final List<? extends Processor> processorList) {
      return CPU.builder().count(processorList.isEmpty() ? 1 : processorList.size())
            .speed(determineDimensionDataCpuSpeed(processorList)).coresPerSocket(determineCoresPerSocket(processorList))
            .build();
   }

   private String determineDimensionDataCpuSpeed(final List<? extends Processor> processorList) {
      if (processorList.isEmpty()) {
         return CpuSpeed.getDefaultCpuSpeed().getDimensionDataSpeed();
      } else {
         final double firstUserSuppliedSpeed = processorList.get(0).getSpeed();

         for (Processor processor : processorList) {
            if (processor.getSpeed() != firstUserSuppliedSpeed) {
               return CpuSpeed.getDefaultCpuSpeed().getDimensionDataSpeed();
            }
         }
         return CpuSpeed.fromJCloudsSpeed(firstUserSuppliedSpeed).getDimensionDataSpeed();
      }
   }

   private int determineCoresPerSocket(final List<? extends Processor> processorList) {
      if (processorList.isEmpty()) {
         return 1;
      } else {
         final double firstUserSuppliedCores = processorList.get(0).getCores();
         for (Processor processor : processorList) {
            if (processor.getCores() != firstUserSuppliedCores) {
               return 1;
            }
         }
         return (int) firstUserSuppliedCores;
      }
   }
}
