package com.joyfulmongo.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public abstract class JFMongoCmd
{
  /**
   * prepare
   */
  protected abstract void beforeExecute();
  
  /**
   * execute
   * 
   * @return
   */
  protected abstract JFMongoCmdResult execute();
  
  /**
   * post execute
   * 
   * @return
   */
  protected abstract JFMongoCmdResult afterExecute(
      JFMongoCmdResult executeResult);
  
  /**
   * 
   * @return
   */
  public JFMongoCmdResult invoke()
  {
    beforeExecute();
    JFMongoCmdResult executeResult = execute();
    JFMongoCmdResult result = afterExecute(executeResult);
    return result;
  }
  
  protected List<ContainerObject> findChildObject(JSONObject parentObj)
  {
    List<ContainerObject> childs = new ArrayList<ContainerObject>(0);
    Iterator<String> keys = parentObj.keys();
    while (keys.hasNext())
    {
      String key = keys.next();
      Object obj = parentObj.get(key);
      if (obj instanceof JSONObject)
      {
        ContainerObject typedObj = ContainerObjectFactory.getChildObject(key,
            (JSONObject) obj);
        if (typedObj != null)
        {
          childs.add(typedObj);
        }
      }
    }
    
    return childs;
  }
}
