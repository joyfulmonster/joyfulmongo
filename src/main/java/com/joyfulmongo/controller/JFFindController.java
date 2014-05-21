package com.joyfulmongo.controller;

import java.util.List;

import com.joyfulmongo.db.JFMongoCmdQuery;
import org.json.JSONObject;

import com.joyfulmongo.db.JFMongoObject;

public class JFFindController extends JFController<JFFindInput, JFFindOutput>
{
  @Override
  public JFFindOutput process(JFFindInput input)
  {
    String colName = input.getClassname();
    
    JSONObject condition = input.getData();
    String sort = input.getOrder();
    String[] includes = input.getIncludes();
    
    int limit = input.getLimit();
    int skip = input.getSkip();
    
    String redirectClassNameForKey = input.getRedirectClassNameForKey();
    
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(colName)
        .constraints(condition).limit(limit).skip(skip).include(includes)
        .sort(sort).redirectClassNameForKey(redirectClassNameForKey).build();
    
    List<JFMongoObject> results = query.find();
    
    if (results == null)
    {
      throw new JFUserError(101, "object not found for get");
    } else
    {
      JFFindOutput output = new JFFindOutput(results);
      return output;
    }
  }
}
