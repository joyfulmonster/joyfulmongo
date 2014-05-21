package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdResult;
import com.joyfulmongo.db.JFMongoCmdUpdate;
import org.json.JSONObject;

public class JFUpdateController extends
    JFController<JFUpdateInput, JFUpdateOutput>
{
  @Override
  public JFUpdateOutput process(JFUpdateInput input)
  {
    String collection = input.getClassname();
    
    JSONObject payload = input.getData();
    
    JFMongoCmdUpdate dbObj = new JFMongoCmdUpdate(collection, payload);
    
    JFMongoCmdResult commandResult = dbObj.invoke();
    
    JFUpdateOutput result = new JFUpdateOutput(commandResult);
    
    return result;
  }
}
