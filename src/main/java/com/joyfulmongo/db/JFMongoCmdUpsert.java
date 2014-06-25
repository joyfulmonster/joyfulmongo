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

class JFMongoCmdUpsert extends JFCommand
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
    
    MongoCollection collection = MongoCollectionFactory.getInstance()
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
