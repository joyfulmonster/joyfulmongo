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

public abstract class JFInput extends JSONObject
{
  public JFInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public JFInput()
  {
    super();
  }
  
  public String getClassname()
  {
    String str = getString(JFCConstants.Props.classname.toString());
    return str;
  }
  
  public String getSession_token()
  {
    return getString(JFCConstants.Props.session_token.toString());
  }
  
  public String getIid()
  {
    return getString(JFCConstants.Props.iid.toString());
  }
  
  public String getUuid()
  {
    return getString(JFCConstants.Props.uuid.toString());
  }
  
  public String getV()
  {
    return getString(JFCConstants.Props.v.toString());
  }
}
