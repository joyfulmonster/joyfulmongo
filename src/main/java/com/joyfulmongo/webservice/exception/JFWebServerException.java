package com.joyfulmongo.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class JFWebServerException extends WebApplicationException {
    private static final long serialVersionUID = -2014257373418972011L;

    public JFWebServerException() {
        super(Response.ok().build());
    }

    public JFWebServerException(String message) {
        super(Response.ok().entity(message).type(MediaType.APPLICATION_JSON).build());
    }

    public JFWebServerException(Exception cause) {
        super(cause, Response.ok().entity(cause.getMessage()).type(MediaType.APPLICATION_JSON).build());
    }

    public JFWebServerException(Status status, String message) {
        super(Response.ok().entity(message).type(MediaType.APPLICATION_JSON).build());
    }
}