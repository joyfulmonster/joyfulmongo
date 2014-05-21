package com.joyfulmongo.db;

import java.util.Date;

import org.json.JSONObject;

import com.joyfulmongo.db.javadriver.JFDBCollection;
import com.joyfulmongo.db.javadriver.JFDBCollectionFactory;

class JFMongoCmdUpsert extends JFMongoCmd
{
  private JSONObject updates;
  private JSONObject query;
  private String colname;
  
  JFMongoCmdUpsert(String colname, JSONObject query, JSONObject updates)
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
    
    Date now = Utils.getCurrentTime();
    updates.put(Constants.Props.updatedAt.toString(), now);
    
    JFDBCollection collection = JFDBCollectionFactory.getInstance()
        .getCollection(colname);
    collection.upsert(query, updates);
    
    result.setUpdatedAt(now);
    
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return executeResult;
  }
}
