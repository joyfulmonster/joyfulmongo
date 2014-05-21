package com.joyfulmongo.webservice;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import com.joyfulmongo.controller.JFResult;

public class BaseResource
{
  protected Response getResponse(JFResult result)
  {
    GenericEntity<String> entity = new GenericEntity<String>(result + ""){};
    Response res = Response.ok().entity(entity).build();
    return res;
  }
  
  protected Response getResponse(String result)
  {
    GenericEntity<String> entity = new GenericEntity<String>(result){};
    Response res = Response.ok().entity(entity).build();
    return res;
  }  
}
