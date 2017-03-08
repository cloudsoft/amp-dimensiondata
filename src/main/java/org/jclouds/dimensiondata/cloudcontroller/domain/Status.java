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

import com.google.auto.value.AutoValue;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class Status
{
    Status()
    {
    }

    public abstract ResultType result();

    public abstract String resultDetail();

    public abstract String resultCode();

    public abstract List<AdditionalInformationType> additionalInformation();

    @SerializedNames({"result", "resultDetail", "resultCode", "additionalInformation"})
    public static Status create(ResultType result, String resultDetail, String resultCode, List<AdditionalInformationType> additionalInformation)
    {
        return builder().result(result).resultDetail(resultDetail).resultCode(resultCode)
                .additionalInformation(additionalInformation).build();
    }

    public abstract Status.Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder
    {
        public abstract Status.Builder result(ResultType result);

        public abstract Status.Builder resultDetail(String result);

        public abstract Status.Builder resultCode(String resultCode);

        public abstract Status.Builder additionalInformation(List<AdditionalInformationType> additionalInformation);

        public abstract Status build();
    }

    public static Status.Builder builder()
    {
        return new AutoValue_Status.Builder();
    }
}
