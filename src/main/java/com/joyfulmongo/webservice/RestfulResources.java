package com.joyfulmongo.webservice;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author wbao
 *
 */
@Path("/rest")
public class RestfulResources extends BaseResource
{
  @Path("user")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response createUser(String params)
  {    
    return null;
  }
  
  @Path("login")
  @GET
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response loginUser(@QueryParam("username") String phone, @QueryParam("password") String password)
  {    
    return null;        
  }
  
  @Path("classes/{classname}")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({Constants.JF_JSON})
  public Response createObject(@PathParam("classname") String classname, String payload)
  {    
    return null;        
  }
  
  @Path("classes/{classname}")
  @PUT
  @Consumes(Constants.JF_JSON)
  @Produces({Constants.JF_JSON})
  public Response updateObject(@PathParam("classname") String classname, String payload)
  {    
    return null;        
  }  

  @Path("classes/{classname}/{objectid}")
  @GET
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response fetchObject(@PathParam("classname") String classname, @PathParam("objectid") String objectid)
  {    
    return null;        
  }
  
  @Path("classes/{classname}")
  @GET
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response queryObjects(@PathParam("classname") String classname, String constraint)
  {    
    return null;        
  }  

  @Path("classes/{classname}/{objectid}")
  @DELETE
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response deleteObject(@PathParam("classname") String classname, @PathParam("objectid") String objectId)
  {    
    return null;        
  }  
}
