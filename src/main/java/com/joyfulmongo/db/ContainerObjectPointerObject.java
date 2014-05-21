package com.joyfulmongo.db;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectPointerObject extends JSONObjectSupport implements
    ContainerObject
{
  public ContainerObjectPointerObject(String key, JSONObject json)
  {
    super(json);
  }
  
  @Override
  public void onCreate(String colname, JSONObject parse)
  {
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parseObject)
  {
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    
  }
}
