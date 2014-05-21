package com.joyfulmongo.db;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpCmdAdd extends OpCmd
{
  public static String CMD_ADD_TO_SET = "$addToSet";
  public static String CMD_EACH = "$each";
  
  public enum AddProps
  {
    objects,
  }
  
  public OpCmdAdd(String key, JSONObject json)
  {
    super(key, json);
  }
  
  @Override
  public void onCreate(String colname, JSONObject parseObject)
  {
    JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
    parseObject.put(key, a);
  }
  
  @Override
  public Object onCreateOutput()
  {
    JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
    return a;
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parseObject)
  {
    JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
    JSONObject each = new JSONObject();
    each.put(CMD_EACH, a);
    
    JSONObject addToSet = parseObject.optJSONObject(CMD_ADD_TO_SET);
    if (addToSet == null)
    {
      JSONObject field = new JSONObject();
      field.put(key, each);
      
      parseObject.remove(key);
      parseObject.put(CMD_ADD_TO_SET, field);
    } else
    {
      parseObject.remove(key);
      addToSet.put(key, each);
    }
  }
  
  @Override
  public Object onUpdateOutput()
  {
    JSONArray a = mObj.optJSONArray(AddProps.objects.toString());
    return a;
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    // TODO Auto-generated method stub
    
  }
  
}
