/*
 * Copyright 2014 Weifeng Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package com.joyfulmongo.webservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.joyfulmongo.controller.JFController;
import com.joyfulmongo.controller.JFCreateInput;
import com.joyfulmongo.controller.JFCreateOutput;
import com.joyfulmongo.controller.JFDeleteInput;
import com.joyfulmongo.controller.JFDeleteOutput;
import com.joyfulmongo.controller.JFFindInput;
import com.joyfulmongo.controller.JFFindOutput;
import com.joyfulmongo.controller.JFGetInput;
import com.joyfulmongo.controller.JFGetOutput;
import com.joyfulmongo.controller.JFLoginInput;
import com.joyfulmongo.controller.JFLoginOutput;
import com.joyfulmongo.controller.JFMultiInput;
import com.joyfulmongo.controller.JFMultiOutput;
import com.joyfulmongo.controller.JFResult;
import com.joyfulmongo.controller.JFSignupInput;
import com.joyfulmongo.controller.JFSignupOutput;
import com.joyfulmongo.controller.JFUpdateInput;
import com.joyfulmongo.controller.JFUpdateOutput;

@Path("/program")
public class ParseProgramResources extends BaseResource
{
  private static Logger LOGGER = Logger.getLogger(ParseProgramResources.class.getName());
  
  @Path("user_signup")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response signup(String input)
  {
    LOGGER.log(Level.FINER, "signup input=" + input);
    JFSignupInput signupInput = new JFSignupInput(input);
    JFController<JFSignupInput, JFSignupOutput> controller = JFController
        .getInstance(JFSignupInput.class, JFSignupOutput.class);
    JFResult result = controller.processWrapper(signupInput);
    LOGGER.log(Level.FINER, "signup result=" + result);
    return getResponse(result);
  }
  
  @Path("user_login")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response login(String input)
  {
    LOGGER.log(Level.FINE, "login input=" + input);
    JFLoginInput loginInput = new JFLoginInput(input);
    JFController<JFLoginInput, JFLoginOutput> controller = JFController
        .getInstance(JFLoginInput.class, JFLoginOutput.class);
    JFResult result = controller.processWrapper(loginInput);
    LOGGER.log(Level.FINE, "login result=" + result);
    return getResponse(result);
  }
  
  @Path("create")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response create(String input)
  {
    LOGGER.log(Level.FINER, "create input=" + input);
    JFCreateInput createInput = new JFCreateInput(input);
    JFController<JFCreateInput, JFCreateOutput> controller = JFController
        .getInstance(JFCreateInput.class, JFCreateOutput.class);
    JFResult result = controller.processWrapper(createInput);
    LOGGER.log(Level.FINER, "create result=" + result);
    return getResponse(result);
  }
  
  @Path("update")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response update(String input)
  {
    LOGGER.log(Level.FINER, "update input=" + input);
    JFUpdateInput updateInput = new JFUpdateInput(input);
    JFController<JFUpdateInput, JFUpdateOutput> controller = JFController
        .getInstance(JFUpdateInput.class, JFUpdateOutput.class);
    JFResult result = controller.processWrapper(updateInput);
    LOGGER.log(Level.FINER, "update result=" + result);
    return getResponse(result);
  }
  
  @Path("delete")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response delete(String input)
  {
    LOGGER.log(Level.FINER, "delete input=" + input);
    JFDeleteInput deleteInput = new JFDeleteInput(input);
    JFController<JFDeleteInput, JFDeleteOutput> controller = JFController
        .getInstance(JFDeleteInput.class, JFDeleteOutput.class);
    JFResult result = controller.processWrapper(deleteInput);
    LOGGER.log(Level.FINER, "delete result=" + result);
    return getResponse(result);
  }
  
  @Path("multi")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response multi(String input)
  {
    LOGGER.log(Level.FINER, "multi input=" + input);
    JFMultiInput multiInput = new JFMultiInput(input);
    JFController<JFMultiInput, JFMultiOutput> controller = JFController
        .getInstance(JFMultiInput.class, JFMultiOutput.class);
    JFResult result = controller.processWrapper(multiInput);
    LOGGER.log(Level.FINER, "multi result=" + result);
    return getResponse(result);
  }
  
  @Path("get")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response get(String input)
  {
    LOGGER.log(Level.FINER, "get input=" + input);
    JFGetInput getInput = new JFGetInput(input);
    JFController<JFGetInput, JFGetOutput> controller = JFController
        .getInstance(JFGetInput.class, JFGetOutput.class);
    JFResult result = controller.processWrapper(getInput);
    LOGGER.log(Level.FINER, "get result=" + result);
    return getResponse(result);
  }
  
  @Path("find")
  @POST
  @Consumes(Constants.JF_JSON)
  @Produces({ Constants.JF_JSON })
  public Response find(String input)
  {
    LOGGER.log(Level.FINER, "find input=" + input);
    JFFindInput findInput = new JFFindInput(input);
    JFController<JFFindInput, JFFindOutput> controller = JFController
        .getInstance(JFFindInput.class, JFFindOutput.class);
    JFResult result = controller.processWrapper(findInput);
    LOGGER.log(Level.FINER, "find result=" + result);
    return getResponse(result);
  }
}
