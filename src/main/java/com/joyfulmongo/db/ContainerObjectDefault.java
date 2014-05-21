package com.joyfulmongo.db;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectDefault extends JSONObjectSupport implements
    ContainerObject
{
  public ContainerObjectDefault(String key, JSONObject json)
  {
    super(json);
  }
  
  @Override
  public void onCreate(String colname, JSONObject parse)
  {
    // do nothing
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parseObject)
  {
    // do nothing
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    
  }
}
