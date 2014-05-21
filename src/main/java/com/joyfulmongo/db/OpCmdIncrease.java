package com.joyfulmongo.db;

import org.json.JSONObject;

public class OpCmdIncrease extends OpCmd
{
  public static final String CMD_INC = "$inc";
  
  public enum IncProps
  {
    Increment, amount,
  }
  
  public OpCmdIncrease(String key, JSONObject json)
  {
    super(key, json);
  }
  
  @Override
  public void onCreate(String colname, JSONObject parseObject)
  {
    onUpdate(colname, parseObject);
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parseObject)
  {
    Double amount = mObj.getDouble(IncProps.amount.toString());
    JSONObject field = new JSONObject();
    field.put(key, amount);
    
    parseObject.remove(key);
    parseObject.put(CMD_INC, field);
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    // TODO Auto-generated method stub
    
  }
}
