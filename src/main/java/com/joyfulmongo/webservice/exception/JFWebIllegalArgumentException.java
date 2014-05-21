package com.joyfulmongo.webservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class JFWebIllegalArgumentException extends WebApplicationException 
{	 
  private static final long serialVersionUID = 9083780475637097927L;

  public JFWebIllegalArgumentException() 
  {
    super(Response.ok().build());
  }
 
  public JFWebIllegalArgumentException(String message) 
  {
    super(Response.ok().entity(message)
    		.type(MediaType.APPLICATION_JSON).build());
  }
}