package com.joyfulmongo.controller;

import org.json.JSONObject;

class JFSingleDataInput extends JFInput
{
  protected JFSingleDataInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public JFSingleDataInput(String colname, JSONObject data)
  {
    super();
    put(JFCConstants.Props.classname.toString(), colname);
    put(JFCConstants.Props.data.toString(), data);
  }
  
  public JSONObject getData()
  {
    JSONObject result;
    Object data = get(JFCConstants.Props.data.toString());
    
    if (data instanceof JSONObject)
    {
      result = (JSONObject) data;
    } 
    else if (data instanceof String)
    {
      result = new JSONObject((String) data);
    } 
    else
    {
      throw new IllegalArgumentException("The data field has invalid value "
          + data.getClass());
    }
    
    return result;
  }
}
