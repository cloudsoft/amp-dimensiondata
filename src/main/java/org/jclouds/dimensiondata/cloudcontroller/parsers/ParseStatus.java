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

import org.jclouds.dimensiondata.cloudcontroller.domain.Status;
import org.jclouds.http.functions.ParseSax;

import static org.jclouds.util.SaxUtils.currentOrNull;

public class ParseStatus extends ParseSax.HandlerForGeneratedRequestWithResult<Status>  {

   private StringBuilder currentText = new StringBuilder();

   private Status.ResultType result;

   private String resultDetail;

   private String resultCode;

   private String operation;

   @Override
   public Status getResult() {
      return Status.create(result, resultDetail, resultCode, null, operation);
   }

   @Override
   public void endElement(String ignoredUri, String ignoredName, String qName)
   {
       if (qName.endsWith("result"))
       {
          result =  Status.ResultType.fromString(currentOrNull(currentText));
       }
       if (qName.endsWith("resultDetail"))
       {
          resultDetail = currentOrNull(currentText);
       }
       if (qName.endsWith("resultCode"))
       {
          resultCode = currentOrNull(currentText);
       }
       if (qName.endsWith("operation"))
       {
          operation = currentOrNull(currentText);
       }
       currentText.setLength(0);
   }

   @Override
   public void characters(char ch[], int start, int length)
   {
       currentText.append(ch, start, length);
   }
}
