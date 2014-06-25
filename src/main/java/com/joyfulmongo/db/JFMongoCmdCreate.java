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

import com.joyfulmongo.db.javadriver.MongoCollection;
import com.joyfulmongo.db.javadriver.MongoCollectionFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;

public class JFMongoCmdCreate extends JFCommand
{
  private String collectionName;
  private Date now;
  private String newObjectId;
  private JSONObject mInput;
  
  public JFMongoCmdCreate(JFMongoObject obj)
  {
    this.collectionName = obj.getCollectionName();
    this.mInput = obj.toJson();
  }
  
  public JFMongoCmdCreate(String colname, JSONObject json)
  {
    this.collectionName = colname;
    this.mInput = json;
  }
  
  protected JSONObject getPayload()
  {
    return this.mInput;
  }
  
  @Override
  protected void beforeExecute()
  {
    String objectId = mInput.optString(Constants.Props.objectId.toString());
    if (objectId != null && objectId.length() > 0)
    {
      throw new IllegalArgumentException(
          "Insert object should have have ObjectId.");
    } else
    {
      newObjectId = generateObjectId();
      mInput.put(Constants.Props.objectId.toString(), newObjectId);
    }
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    JFMongoCmdResult result = new JFMongoCmdResult();
    
    List<ContainerObject> childs = findChildObject(mInput);
    for (ContainerObject child : childs)
    {
      child.onCreate(collectionName, mInput);
      if (child instanceof OpCmd)
      {
        OpCmd cmd = (OpCmd) child;
        Object o = cmd.onCreateOutput();
        String key = cmd.getKey();
        result.put(key, o);
      }
    }
    
    now = Utils.getCurrentTime();
    mInput.put(Constants.Props.createdAt.toString(), now);
    mInput.put(Constants.Props.updatedAt.toString(), now);
    
    MongoCollection collection = MongoCollectionFactory.getInstance()
        .getCollection(collectionName);
    collection.create(mInput);
    
    result.setCreatedAt(now);
    result.setObjectId(newObjectId);
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult execResult)
  {
    return execResult;
  }
  
  protected String generateObjectId()
  {
    String objectId = null;
    
    while (objectId == null)
    {
      objectId = RandomStringUtils.randomAlphanumeric(10);
      boolean unique = verifyObjectIdUnique(objectId);
      if (unique)
      {
        break;
      } 
      else
      {
        objectId = null;
      }
    }
    
    return objectId;
  }
  
  protected boolean verifyObjectIdUnique(String objectId)
  {
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(this.collectionName)
        .projection(Constants.Props.objectId.toString())
        .whereEquals(Constants.Props.objectId.toString(), objectId).build();
    
    List<JFMongoObject> results = query.find();
    return results.size() == 0;
  }
}
