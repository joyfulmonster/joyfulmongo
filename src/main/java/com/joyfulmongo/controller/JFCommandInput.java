package com.joyfulmongo.controller;

import org.json.JSONObject;

class JFCommandInput extends JSONObjectSupport
{
  public enum Operation
  {
    create, update,
  }
  
  public JFCommandInput(JSONObject json)
  {
    super(json);
  }
  
  private JSONObject params;
  
  public String getSessionToken()
  {
    String result = mObj.getString(JFCConstants.Props.session_token.toString());
    return result;
  }
  
  public Operation getOp()
  {
    String result = mObj.getString(JFCConstants.Props.op.toString());
    Operation op = Operation.valueOf(result);
    return op;
  }
  
  public JSONObject getData()
  {
    if (params == null)
    {
      params = mObj.getJSONObject(JFCConstants.Props.params.toString());
    }
    
    JSONObject result = params
        .getJSONObject(JFCConstants.Props.data.toString());
    
    return result;
  }
  
  public String getClassname()
  {
    if (params == null)
    {
      params = mObj.getJSONObject(JFCConstants.Props.params.toString());
    }
    
    return params.getString(JFCConstants.Props.classname.toString());
  }
}
