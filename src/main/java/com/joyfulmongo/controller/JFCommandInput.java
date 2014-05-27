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

package com.joyfulmongo.controller;

import org.json.JSONObject;

class JFCommandInput extends JSONObjectSupport
{
  public enum Operation
  {
    create, update,
  }
  
  public JFCommandInput(JSONObject json)
  {
    super(json);
  }
  
  private JSONObject params;
  
  public String getSessionToken()
  {
    String result = mObj.getString(JFCConstants.Props.session_token.toString());
    return result;
  }
  
  public Operation getOp()
  {
    String result = mObj.getString(JFCConstants.Props.op.toString());
    Operation op = Operation.valueOf(result);
    return op;
  }
  
  public JSONObject getData()
  {
    if (params == null)
    {
      params = mObj.getJSONObject(JFCConstants.Props.params.toString());
    }
    
    JSONObject result = params
        .getJSONObject(JFCConstants.Props.data.toString());
    
    return result;
  }
  
  public String getClassname()
  {
    if (params == null)
    {
      params = mObj.getJSONObject(JFCConstants.Props.params.toString());
    }
    
    return params.getString(JFCConstants.Props.classname.toString());
  }
}
