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
package org.jclouds.dimensiondata.cloudcontrol.parsers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.ArgsToPagedIterable;
import org.jclouds.dimensiondata.cloudcontrol.DimensionDataCloudControlApi;
import org.jclouds.dimensiondata.cloudcontrol.domain.PublicIpBlock;
import org.jclouds.dimensiondata.cloudcontrol.domain.PublicIpBlocks;
import org.jclouds.dimensiondata.cloudcontrol.options.PaginationOptions;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

@Singleton
public class ParsePublicIpBlocks extends ParseJson<PublicIpBlocks> {

    @Inject
    public ParsePublicIpBlocks(Json json) {
        super(json, TypeLiteral.get(PublicIpBlocks.class));
    }

    public static class ToPagedIterable extends ArgsToPagedIterable<PublicIpBlock, ToPagedIterable> {

        private DimensionDataCloudControlApi api;

        @Inject
        public ToPagedIterable(DimensionDataCloudControlApi api) {
            this.api = api;
        }

        @Override
        protected Function<Object, IterableWithMarker<PublicIpBlock>> markerToNextForArgs(List<Object> args) {
            return new Function<Object, IterableWithMarker<PublicIpBlock>>() {
                @Override
                public IterableWithMarker<PublicIpBlock> apply(Object input) {
                    PaginationOptions paginationOptions = PaginationOptions.class.cast(input);
                    return api.getNetworkApi().listPublicIPv4AddressBlocks(getArgs(request).get(0).toString(), paginationOptions);
                }
            };
        }
    }
}
