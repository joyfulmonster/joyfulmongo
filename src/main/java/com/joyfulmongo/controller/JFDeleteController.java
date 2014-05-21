package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdDelete;
import com.joyfulmongo.db.JFMongoCmdResult;
import org.json.JSONObject;

public class JFDeleteController extends
    JFController<JFDeleteInput, JFDeleteOutput>
{
  @Override
  public JFDeleteOutput process(JFDeleteInput input)
  {
    String collection = input.getClassname();
    
    JSONObject payload = input.getData();
    
    JFMongoCmdDelete cmd = new JFMongoCmdDelete(collection, payload);
    
    JFMongoCmdResult cmdResult = cmd.invoke();
    
    Boolean res = (cmdResult != null);
    
    JFDeleteOutput result = new JFDeleteOutput(res);
    
    return result;
  }
  
}
