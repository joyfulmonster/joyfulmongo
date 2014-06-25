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

import com.joyfulmongo.db.javadriver.MongoCollection;
import com.joyfulmongo.db.javadriver.MongoCollectionFactory;
import org.json.JSONObject;

public class JFMongoCmdDelete extends JFCommand
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
    
    MongoCollection collection = MongoCollectionFactory.getInstance().getCollection(collectionName);
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
