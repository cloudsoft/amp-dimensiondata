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
package org.jclouds.dimensiondata.cloudcontrol.domain;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * A collection of Datacenter
 */
public class Datacenters extends PaginatedCollection<Datacenter> {

   @ConstructorProperties({ "datacenter", "pageNumber", "pageCount", "totalCount", "pageSize" })
   public Datacenters(List<Datacenter> content, Integer pageNumber, Integer pageCount, Integer totalCount,
                      Integer pageSize) {
      super(content, pageNumber, pageCount, totalCount, pageSize);
   }
}
