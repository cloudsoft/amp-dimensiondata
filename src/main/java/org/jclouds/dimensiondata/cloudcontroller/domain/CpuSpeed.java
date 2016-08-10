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

/**
 * Defines the CPU speeds available in the Dimension Data Cloud.
 */
public enum CpuSpeed {

   /**
    * Dimension Data Economy speed CPU.
    * <p/>
    * May not be available in all datacenters.
    */
   ECONOMY(200),

   /**
    * Dimension Data Standard speed CPU.
    * <p/>
    * The default CPU speed if not otherwise specified.
    */
   STANDARD(339),

   /**
    * Dimension Data High-Performance speed CPU.
    * <p/>
    * May not be available in all datacenters.
    */
   HIGHPERFORMANCE(800);

   /**
    * The values used to represent this CPU speed in JClouds.
    */
   private final double jCloudsSpeed;

   CpuSpeed(double jCloudsSpeed) {
      this.jCloudsSpeed = jCloudsSpeed;
   }

   /**
    * Gets the default CPU speed used in the Dimension Data cloud.
    *
    * @return the default speed.
    */
   public static CpuSpeed getDefaultCpuSpeed() {
      return STANDARD;
   }

   /**
    * Gets the CPU speed that corresponds to the supplied required speed.
    * <p/>
    * If there is no matching speed for the supplied value then the {@link #getDefaultCpuSpeed()} speed is returned.
    *
    * @param requiredSpeed the CPU speed that is required.
    * @return the corresponding CPU speed.
    */
   public static CpuSpeed fromJCloudsSpeed(final double requiredSpeed) {
      for (CpuSpeed cpuSpeed : CpuSpeed.values()) {
         if (cpuSpeed.jCloudsSpeed == requiredSpeed) {
            return cpuSpeed;
         }
      }
      return getDefaultCpuSpeed();
   }

   /**
    * Gets the CPU speed that corresponds to the supplied required speed.
    * <p/>
    * If there is no matching speed for the supplied value then the {@link #getDefaultCpuSpeed()} speed is returned.
    *
    * @param requiredSpeed the CPU speed that is required.
    * @return the corresponding CPU speed.
    */
   public static CpuSpeed fromDimensionDataSpeed(final String requiredSpeed) {
      for (CpuSpeed cpuSpeed : CpuSpeed.values()) {
         if (cpuSpeed.getDimensionDataSpeed().equals(requiredSpeed)) {
            return cpuSpeed;
         }
      }
      return getDefaultCpuSpeed();
   }

   /**
    * Gets the CPU speed as represented in JClouds.
    *
    * @return the CPU speed.
    */
   public double getJCloudsSpeed() {
      return jCloudsSpeed;
   }

   /**
    * Gets the CPU speed as represented in the Dimension Data cloud.
    *
    * @return the CPU speed.
    */
   public String getDimensionDataSpeed() {
      return name();
   }
}
