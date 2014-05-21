package com.joyfulmongo.db;

import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectPointer extends JSONObjectSupport implements
    ContainerObject
{
  public enum Props
  {
    objectId, className;
  }
  
  ContainerObjectPointer(String key, JSONObject json)
  {
    super(json);
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
  
  public String getClassName()
  {
    return mObj.getString(Props.className.toString());
  }
  
  public String getObjectId()
  {
    return mObj.getString(Props.objectId.toString());
  }
  
  public JFMongoObject getRefereeObject()
  {
    JFMongoCmdQuery.Builder queryBuilder = new JFMongoCmdQuery.Builder(
        getClassName());
    
    queryBuilder
        .whereEquals(Constants.Props.objectId.toString(), getObjectId());
    
    List<JFMongoObject> parseObjs = queryBuilder.build().find();
    
    JFMongoObject result = null;
    if (parseObjs.size() > 0)
    {
      result = parseObjs.get(0);
    } else
    {
      throw new IllegalArgumentException(
          "The pointer points to a non existing objectid " + getObjectId());
    }
    
    return result;
  }
  
  public void replaceObject(JFMongoObject refereeObject)
  {
    mObj.put(ContainerObjectFactory.TypeProps.__type.toString(),
        ContainerObjectFactory.TypeType.Object.toString());
    mObj.remove(Props.objectId.toString());
    JSONObject theObj = refereeObject.toJson();
    Iterator<String> keys = theObj.keys();
    while (keys.hasNext())
    {
      String key = keys.next();
      mObj.put(key, theObj.get(key));
    }
  }
}
