package com.joyfulmongo.db;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class JFMongoCmdResult extends JSONObject
{
  JFMongoCmdResult()
  {
    super();
  }
  
  void setObjectId(String objectId)
  {
    put(Constants.Props.objectId.toString(), objectId);
  }
  
  void setCreatedAt(Date time)
  {
    String timeStr = Utils.getParseDateFormat().format(time);
    put(Constants.Props.createdAt.toString(), timeStr);
  }
  
  void setUpdatedAt(Date time)
  {
    String timeStr = Utils.getParseDateFormat().format(time);
    put(Constants.Props.updatedAt.toString(), timeStr);
  }
  
  void addData(String key, JSONObject data)
  {
    put(key, data);
  }
  
  void addData(String key, JSONArray array)
  {
    put(key, array);
  }
}
