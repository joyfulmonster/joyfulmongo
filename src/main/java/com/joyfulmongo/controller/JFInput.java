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
