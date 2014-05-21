package com.joyfulmongo.db;

import org.json.JSONObject;

public interface ContainerObject
{
  
  /**
   * Callback on create
   * 
   * @param parseObject
   */
  void onCreate(String collectionName, JSONObject parseObject);
  
  /**
   * Callback on update
   * 
   * @param parseObject
   */
  void onUpdate(String collectionName, JSONObject parseObject);
  
  /*
   * 
   */
  void onQuery(String collectionName, JSONObject parseObject);
}
