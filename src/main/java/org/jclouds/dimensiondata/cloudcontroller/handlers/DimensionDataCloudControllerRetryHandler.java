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
package org.jclouds.dimensiondata.cloudcontroller.handlers;

import com.google.common.annotations.Beta;
import com.google.inject.Inject;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;
import org.jclouds.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;
import static org.jclouds.http.HttpUtils.closeClientButKeepContentStream;

/**
 * Retry handler that takes into account the DimensionData retriers and delays
 * the requests until they are known to succeed.
 */
@Beta
@Singleton
public class DimensionDataCloudControllerRetryHandler extends BackoffLimitedRetryHandler {

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject(optional = true)
   @Named(PROPERTY_MAX_RETRIES)
   private int retryCountLimit = 5;

   @Override
   public boolean shouldRetryRequest(final HttpCommand command, final HttpResponse response) {
      command.incrementFailureCount();

      byte[] data = closeClientButKeepContentStream(response);
      String message = data != null ? new String(data) : null;
      // Do not retry client errors that are not retryable errors
      if (response.getStatusCode() != 400 &&
              (!message.contains("RESOURCE_BUSY") || !message.contains("UNEXPECTED_ERROR"))) {
         return false;
      } else if (!command.isReplayable()) {
         logger.error("Cannot retry after server error, command is not replayable: %1$s", command);
         return false;
      } else if (command.getFailureCount() > retryCountLimit) {
         logger.error("Cannot retry after server error, command has exceeded retry limit %1$d: %2$s", retryCountLimit,
               command);
         return false;
      } else if (response.getStatusCode() == 400 && (
            message.contains("CPU_SPEED_NOT_AVAILABLE") ||
            message.contains("CONFIGURATION_NOT_SUPPORTED"))) {
         return false;
      } else {
         imposeBackoffExponentialDelay(command.getFailureCount(), "server error: " + command.toString());
         return true;
      }
   }

}
