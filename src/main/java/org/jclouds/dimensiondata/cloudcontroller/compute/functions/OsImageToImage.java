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
package org.jclouds.dimensiondata.cloudcontroller.compute.functions;

import static com.google.common.collect.Iterables.get;

import java.util.Set;

import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.dimensiondata.cloudcontroller.domain.OsImage;
import org.jclouds.domain.Location;
import org.jclouds.location.predicates.LocationPredicates;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

@Singleton
public class OsImageToImage implements Function<OsImage, Image> {

    private static final String CENTOS = "CentOS";
    private static final String REDHAT = "RedHat";
    private static final String UBUNTU = "Ubuntu";
    private static final String SUSE = "SuSE";
    private static final String WINDOWS = "Win";

    private final Supplier<Set<? extends Location>> locations;

    @Inject
    OsImageToImage(@Memoized final Supplier<Set<? extends org.jclouds.domain.Location>> locations) {
        this.locations = locations;
    }

    @Override
    public Image apply(OsImage from) {
        OsFamily osFamily = osFamily().apply(from.description());
        String osVersion = parseVersion(from.description());

        OperatingSystem os = OperatingSystem.builder()
                .description(from.description())
                .family(osFamily)
                .version(osVersion)
                .is64Bit(is64bit(from))
                .build();

        return new ImageBuilder()
                .id(from.id())
                .name(from.name())
                .status(Image.Status.AVAILABLE)
                .operatingSystem(os)
                .location(FluentIterable.from(locations.get()).firstMatch(LocationPredicates.idEquals(from.datacenterId())).orNull())
                .build();
    }

    private boolean is64bit(OsImage inspectedImage) {
        return inspectedImage.operatingSystem().id().contains("64");
    }

    private Function<String, OsFamily> osFamily() {
        return new Function<String, OsFamily>() {

            @Override
            public OsFamily apply(final String description) {
                if (description != null) {
                    if (description.startsWith(CENTOS)) return OsFamily.CENTOS;
                    else if (description.contains(UBUNTU)) return OsFamily.UBUNTU;
                    else if (description.contains(REDHAT)) return OsFamily.RHEL;
                    else if (description.contains(SUSE)) return OsFamily.RHEL;
                    else if (description.contains(WINDOWS)) return OsFamily.WINDOWS;
                }
                return OsFamily.UNRECOGNIZED;
            }
        };
    }

    private String parseVersion(String documentation) {
        String version;
        if (documentation.toLowerCase().startsWith(OsFamily.CENTOS.value().toLowerCase())) {
            version = get(Splitter.on(" ").split(documentation), 2);
        } else if (documentation.toLowerCase().startsWith(OsFamily.SUSE.value().toLowerCase())) {
            version = get(Splitter.on(" ").split(documentation), 4);
        } else if (documentation.split(" ").length > 1) {
            version = get(Splitter.on(" ").split(documentation), 1);
        } else {
            version = "unknown";
        }
        return version;
    }
}
