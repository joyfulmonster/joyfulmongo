package com.joyfulmongo.db;

import java.util.Date;

import org.json.JSONObject;

import com.joyfulmongo.db.javadriver.JFDBCollection;
import com.joyfulmongo.db.javadriver.JFDBCollectionFactory;

public class JFMongoCmdDelete extends JFMongoCmd
{
  private JSONObject mObj;
  private String collectionName;
  
  public JFMongoCmdDelete(String colname, JSONObject json)
  {
    this.mObj = json;
    this.collectionName = colname;
  }
  
  @Override
  protected void beforeExecute()
  {
    String objectId = mObj.optString(Constants.Props.objectId.toString());
    if (objectId == null)
    {
      throw new IllegalArgumentException("Update object must have have ObjectId.");
    }
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    Date now = Utils.getCurrentTime();
    String timeStr = Utils.getParseDateFormat().format(now);
    mObj.put(Constants.Props.updatedAt.toString(), timeStr);
    
    JFDBCollection collection = JFDBCollectionFactory.getInstance().getCollection(collectionName);
    collection.delete(mObj);
    
    JFMongoCmdResult result = new JFMongoCmdResult();
    result.setUpdatedAt(now);
    
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return executeResult;
  }
}
