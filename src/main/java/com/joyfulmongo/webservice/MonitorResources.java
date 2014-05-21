package com.joyfulmongo.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.common.net.MediaType;
import com.joyfulmongo.monitor.MonitorManager;

@Path("/console")
public class MonitorResources extends BaseResource
{
  @Path("query")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces("text/plain")
  public Response queryLog(String params)
  {   
    String result = null;
    try
    {
      result = MonitorManager.getInstance().diagnosticQueries();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return getResponse(result);
  }
}  
