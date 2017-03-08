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
public abstract class Roles
{
    public abstract List<RoleType> role();

    Roles()
    {
    }

    public static Roles.Builder builder()
    {
        return new AutoValue_Roles.Builder();
    }

    @SerializedNames({"role"})
    public static Roles create(List<RoleType> role)
    {
        return builder().role(role).build();
    }

    public abstract Roles.Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder
    {

        public abstract Roles.Builder role(List<RoleType> role);

        public abstract Roles build();
    }

}
///**
// * <p>Java class for Roles complex type.
// *
// * <p>The following schema fragment specifies the expected content contained within this class.
// *
// * <pre>
// * &lt;complexType name="Roles"&gt;
// *   &lt;complexContent&gt;
// *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
// *       &lt;sequence&gt;
// *         &lt;element name="role" type="{http://oec.api.opsource.net/schemas/directory}RoleType" maxOccurs="unbounded" minOccurs="0"/&gt;
// *       &lt;/sequence&gt;
// *     &lt;/restriction&gt;
// *   &lt;/complexContent&gt;
// * &lt;/complexType&gt;
// * </pre>
// *
// *
// */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "Roles", namespace = "http://oec.api.opsource.net/schemas/directory", propOrder = {
//    "role"
//})
//public class Roles {
//
//    protected List<RoleType> role;
//
//    /**
//     * Gets the value of the role property.
//     *
//     * <p>
//     * This accessor method returns a reference to the live list,
//     * not a snapshot. Therefore any modification you make to the
//     * returned list will be present inside the JAXB object.
//     * This is why there is not a <CODE>set</CODE> method for the role property.
//     *
//     * <p>
//     * For example, to add a new item, do as follows:
//     * <pre>
//     *    getRole().add(newItem);
//     * </pre>
//     *
//     *
//     * <p>
//     * Objects of the following type(s) are allowed in the list
//     * {@link RoleType }
//     *
//     *
//     */
//    public List<RoleType> getRole() {
//        if (role == null) {
//            role = new ArrayList<RoleType>();
//        }
//        return this.role;
//    }
//
//}
