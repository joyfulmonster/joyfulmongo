package com.joyfulmongo.db;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.joyfulmongo.db.javadriver.JFDBCollection;
import com.joyfulmongo.db.javadriver.JFDBCollectionFactory;

public class JFMongoCmdUpdate extends JFMongoCmd
{
  private static Logger LOGGER = Logger.getLogger(JFMongoCmdUpdate.class.getName());
  private JSONObject updates;
  private JSONObject query;
  private String colname;
  
  public JFMongoCmdUpdate(String colname, JSONObject updates)
  {
    String objectId = updates.optString(Constants.Props.objectId.toString());
    if (objectId == null)
    {
      throw new IllegalArgumentException(
          "Update object must have have ObjectId.");
    }
    this.query = new JSONObject();
    query.put(Constants.Props.objectId.toString(), objectId);
    
    this.updates = updates;
    this.colname = colname;
  }
  
  public JFMongoCmdUpdate(String colname, JSONObject query, JSONObject updates)
  {
    this.query = query;
    this.updates = updates;
    this.colname = colname;
  }
  
  @Override
  protected void beforeExecute()
  {
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    JFMongoCmdResult result = new JFMongoCmdResult();
    
    List<ContainerObject> childs = findChildObject(updates);
    for (ContainerObject child : childs)
    {      
      child.onUpdate(colname, updates);
      
      if (child instanceof OpCmd)
      {
        OpCmd cmd = (OpCmd) child;
        Object o = cmd.onCreateOutput();
        String key = cmd.getKey();
        result.put(key, o);
      }
    }
    
    if (LOGGER.isLoggable(Level.FINE))
    {
      LOGGER.log(Level.FINE, " OnUpdate=" + updates);
    }
    
    Date now = Utils.getCurrentTime();
    updates.put(Constants.Props.updatedAt.toString(), now);
    
    JFDBCollection collection = JFDBCollectionFactory.getInstance()
        .getCollection(colname);
    collection.update(query, updates);
    
    result.setUpdatedAt(now);
    
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return executeResult;
  }
}
