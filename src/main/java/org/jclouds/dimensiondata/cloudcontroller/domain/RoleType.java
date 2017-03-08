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

@AutoValue
public abstract class RoleType
{
    public abstract String name();

    RoleType()
    {
    }

    public static RoleType.Builder builder()
    {
        return new AutoValue_RoleType.Builder();
    }

    @SerializedNames({"role"})
    public static RoleType create(String name)
    {
        return builder().name(name).build();
    }

    public abstract RoleType.Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder
    {

        public abstract RoleType.Builder name(String name);

        public abstract RoleType build();
    }

}

///**
// * <p>Java class for RoleType complex type.
// *
// * <p>The following schema fragment specifies the expected content contained within this class.
// *
// * <pre>
// * &lt;complexType name="RoleType"&gt;
// *   &lt;complexContent&gt;
// *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
// *       &lt;sequence&gt;
// *         &lt;element name="name" minOccurs="0"&gt;
// *           &lt;simpleType&gt;
// *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
// *               &lt;maxLength value="256"/&gt;
// *             &lt;/restriction&gt;
// *           &lt;/simpleType&gt;
// *         &lt;/element&gt;
// *       &lt;/sequence&gt;
// *     &lt;/restriction&gt;
// *   &lt;/complexContent&gt;
// * &lt;/complexType&gt;
// * </pre>
// *
// *
// */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "RoleType", namespace = "http://oec.api.opsource.net/schemas/directory", propOrder = {
//    "name"
//})
//public class RoleType {
//
//    protected String name;
//
//    /**
//     * Gets the value of the name property.
//     *
//     * @return
//     *     possible object is
//     *     {@link String }
//     *
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * Sets the value of the name property.
//     *
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *
//     */
//    public void setName(String value) {
//        this.name = value;
//    }
//}
