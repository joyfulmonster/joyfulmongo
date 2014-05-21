package com.joyfulmongo.db;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

abstract class OpCmd extends JSONObjectSupport implements ContainerObject
{
  protected String key;
  
  OpCmd(String key, JSONObject json)
  {
    super(json);
    this.key = key;
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public Object onCreateOutput()
  {
    return null;
  }
  
  public Object onUpdateOutput()
  {
    return null;
  }
}
