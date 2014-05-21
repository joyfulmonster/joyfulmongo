package com.joyfulmongo.controller;

import org.json.JSONObject;

public class JFData extends JSONObject
{
  protected JFData()
  {
    super();
  }
  
  public JFData(JSONObject payload)
  {
    super();
    setPayload(payload);
  }
  
  public void setPayload(JSONObject payload)
  {
    put(JFCConstants.Props.data.toString(), payload);
  }
}
