package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdCreate;
import com.joyfulmongo.db.JFMongoCmdResult;
import org.json.JSONObject;

public class JFCreateController extends
    JFController<JFCreateInput, JFCreateOutput>
{
  @Override
  public JFCreateOutput process(JFCreateInput input)
  {
    String collection = input.getClassname();
    
    JSONObject payload = input.getData();
    
    JFMongoCmdCreate cmd = new JFMongoCmdCreate(collection, payload);
    
    JFMongoCmdResult commandResult = cmd.invoke();
    
    JFCreateOutput result = new JFCreateOutput(commandResult);
    
    return result;
  }
}
