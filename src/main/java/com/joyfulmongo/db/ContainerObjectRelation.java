package com.joyfulmongo.db;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectRelation extends JSONObjectSupport implements
    ContainerObject
{
  private String key;
  
  public enum Props
  {
    className,
  }
  
  public ContainerObjectRelation(String classname)
  {
    super(new JSONObject());
    key = null;
    mObj.put(ContainerObjectFactory.TypeProps.__type.toString(),
        ContainerObjectFactory.TypeType.Relation.toString());
    mObj.put(Props.className.toString(), classname);
  }
  
  public ContainerObjectRelation(String key, JSONObject json)
  {
    super(json);
    this.key = key;
  }
  
  public String getClassname()
  {
    return mObj.getString(Props.className.toString());
  }
  
  @Override
  public void onCreate(String colname, JSONObject parseObject)
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
