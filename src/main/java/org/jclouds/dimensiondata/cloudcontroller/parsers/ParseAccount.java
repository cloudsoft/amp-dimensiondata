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
package org.jclouds.dimensiondata.cloudcontroller.parsers;

import org.jclouds.dimensiondata.cloudcontroller.domain.Account;
import org.jclouds.dimensiondata.cloudcontroller.domain.Roles;
import org.jclouds.http.functions.ParseSax;

import static org.jclouds.util.SaxUtils.currentOrNull;

public class ParseAccount extends ParseSax.HandlerForGeneratedRequestWithResult<Account> /*ParseSax.HandlerWithResult<Account> extends ParseXMLWithJAXB<Account>*/
{

    private StringBuilder currentText = new StringBuilder();

    private String userName;

    private String password;

    private String fullName;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String department;

    private String customDefined1;

    private String customDefined2;

    private String orgId;

    private Roles roles;

    @Override
    public Account getResult()
    {
        Account result = Account.create(userName, password, fullName, firstName, lastName, emailAddress, department,
                customDefined1, customDefined2, orgId, roles);
        return result;
    }

//    @Inject
//    public ParseAccount(XMLParser xml, TypeLiteral<Account> type)
//    {
//        super(xml, type);
//    }

    //    @Override
//    public void startElement(String uri, String localName, String qName, Attributes attributes) {
//       if (qName.equals("userName")) {
//          inConfigurationSets = true;
//       }
//       if (inConfigurationSets) {
//          configurationSetHandler.startElement(uri, localName, qName, attributes);
//       }
//       if (qName.equals("OSVirtualHardDisk")) {
//          inOSVirtualHardDisk = true;
//       }
//       if (qName.equals("DataVirtualHardDisks")) {
//          inDataVirtualHardDisks = true;
//       }
//       if (inDataVirtualHardDisks) {
//          dataVirtualHardDiskHandler.startElement(uri, localName, qName, attributes);
//       }
//       if (qName.equals("ResourceExtensionReference")) {
//          inResourceExtensionReference = true;
//       }
//       if (inResourceExtensionReference) {
//          resourceExtensionReferenceHandler.startElement(uri, localName, qName, attributes);
//       }
//    }
//
    @Override
    public void endElement(String ignoredUri, String ignoredName, String qName)
    {
        if (qName.endsWith("userName"))
        {
            userName = currentOrNull(currentText);
        }
        if (qName.endsWith("password"))
        {
            password = currentOrNull(currentText);
        }
        if (qName.endsWith("fullName"))
        {
            fullName = currentOrNull(currentText);
        }
        if (qName.endsWith("firstName"))
        {
            firstName = currentOrNull(currentText);
        }
        if (qName.endsWith("lastName"))
        {
            lastName = currentOrNull(currentText);
        }
        if (qName.endsWith("userName"))
        {
            userName = currentOrNull(currentText);
        }
        if (qName.endsWith("emailAddress"))
        {
            emailAddress = currentOrNull(currentText);
        }
        if (qName.endsWith("department"))
        {
            department = currentOrNull(currentText);
        }
        if (qName.endsWith("customDefined1"))
        {
            customDefined1 = currentOrNull(currentText);
        }
        if (qName.endsWith("customDefined2"))
        {
            customDefined2 = currentOrNull(currentText);
        }
        if (qName.endsWith("orgId"))
        {
            orgId = currentOrNull(currentText);
        }
        if (qName.endsWith("roles"))
        {
            // TODO need to complete this implementation but this value is not required right now.
        }
        currentText.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length)
    {
        currentText.append(ch, start, length);
    }
}
