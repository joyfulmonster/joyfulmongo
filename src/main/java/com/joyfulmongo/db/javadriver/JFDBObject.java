package com.joyfulmongo.db.javadriver;

import org.json.JSONObject;

import com.mongodb.DBObject;

@SuppressWarnings("serial")
public class JFDBObject
{
  private String collectionName;
  private DBObject dbObject;
  
  public JFDBObject(String collection, JSONObject json)
  {
    collectionName = collection;
    dbObject = JFDBUtil.toDBObject(json);
  }
  
  public JFDBObject(String collection, DBObject obj)
  {
    collectionName = collection;
    dbObject = obj;
  }
  
  public JSONObject toJson()
  {
    JSONObject result = JFDBUtil.toJSONObject(dbObject);
    return result;
  }
  
  public DBObject getDBObject()
  {
    return this.dbObject;
  }
  
  public String getCollectionName()
  {
    return this.collectionName;
  }
}
