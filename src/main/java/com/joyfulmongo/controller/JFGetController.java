package com.joyfulmongo.controller;

import java.util.List;

import com.joyfulmongo.db.JFMongoCmdQuery;
import com.joyfulmongo.db.JFMongoObject;
import org.json.JSONObject;

public class JFGetController extends JFController<JFGetInput, JFGetOutput>
{
  @Override
  public JFGetOutput process(JFGetInput input)
  {
    String collection = input.getClassname();
    
    JSONObject getCondition = input.getData();
    
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(collection)
        .constraints(getCondition).build();
    
    List<JFMongoObject> results = query.find();
    JFMongoObject result = null;
    if (results.size() > 0)
    {
      result = results.get(0);
    }
    
    if (result == null)
    {
      throw new JFUserError(101, "object not found for get");
    } 
    else
    {
      JFGetOutput output = new JFGetOutput(result);
      return output;
    }
  }
}
