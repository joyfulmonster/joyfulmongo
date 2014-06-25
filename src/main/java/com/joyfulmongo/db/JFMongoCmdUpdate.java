/*
 * Copyright 2014 Weifeng Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package com.joyfulmongo.db;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joyfulmongo.db.javadriver.MongoCollection;
import com.joyfulmongo.db.javadriver.MongoCollectionFactory;
import org.json.JSONObject;

public class JFMongoCmdUpdate extends JFCommand
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
    
    MongoCollection collection = MongoCollectionFactory.getInstance()
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
