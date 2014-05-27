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

class JFSingleDataInput extends JFInput
{
  protected JFSingleDataInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public JFSingleDataInput(String colname, JSONObject data)
  {
    super();
    put(JFCConstants.Props.classname.toString(), colname);
    put(JFCConstants.Props.data.toString(), data);
  }
  
  public JSONObject getData()
  {
    JSONObject result;
    Object data = get(JFCConstants.Props.data.toString());
    
    if (data instanceof JSONObject)
    {
      result = (JSONObject) data;
    } 
    else if (data instanceof String)
    {
      result = new JSONObject((String) data);
    } 
    else
    {
      throw new IllegalArgumentException("The data field has invalid value "
          + data.getClass());
    }
    
    return result;
  }
}
