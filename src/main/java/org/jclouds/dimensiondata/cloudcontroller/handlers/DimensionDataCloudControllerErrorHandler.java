package org.jclouds.dimensiondata.cloudcontroller.handlers;

import static org.jclouds.http.HttpUtils.closeClientButKeepContentStream;

import java.io.IOException;

import javax.inject.Singleton;

import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.HttpResponseException;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.ResourceNotFoundException;

import com.google.common.base.Throwables;
import com.google.common.io.Closeables;

/**
 * This will org.jclouds.dimensiondata.cloudcontroller.parse and set an appropriate exception on the command object.
 */
@Singleton
public class DimensionDataCloudControllerErrorHandler implements HttpErrorHandler {

    public void handleError(HttpCommand command, HttpResponse response) {
        // it is important to always read fully and close streams
        byte[] data = closeClientButKeepContentStream(response);

        // Create default exception
        String message = data != null
                ? new String(data)
                : String.format("%s -> %s", command.getCurrentRequest().getRequestLine(), response.getStatusLine());

        Exception exception = message != null ? new HttpResponseException(command, response, message)
                : new HttpResponseException(command, response);
        try {
            message = message != null ? message : String.format("%s -> %s", command.getCurrentRequest().getRequestLine(),
                    response.getStatusLine());
            switch (response.getStatusCode()) {
                case 401:
                case 403:
                    exception = new AuthorizationException(message, exception);
                    break;
                case 404:
                    if (!command.getCurrentRequest().getMethod().equals("DELETE")) {
                        exception = new ResourceNotFoundException(message, exception);
                    }
                    break;
                case 500:
                    if (message != null ){
                        if (message.indexOf("Unable to determine package for") != -1) {
                            exception = new ResourceNotFoundException(message, exception);
                        } else if (message.indexOf("currently an active transaction") != -1) {
                            exception = new IllegalStateException(message, exception);
                        }
                    }
            }
        } finally {
            try {
                Closeables.close(response.getPayload(), true);
            } catch (IOException e) {
                // This code will never be reached
                throw Throwables.propagate(e);
            }
            command.setException(exception);
        }
    }
}