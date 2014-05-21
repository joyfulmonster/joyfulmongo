package com.joyfulmongo.controller;

import org.json.JSONObject;

public abstract class JSONObjectSupport
{
  protected JSONObject mObj;
  
  public JSONObjectSupport(JSONObject json)
  {
    mObj = json;
  }
  
  public JSONObject toJson()
  {
    return mObj;
  }
}
