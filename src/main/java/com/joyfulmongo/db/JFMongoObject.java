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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joyfulmongo.db.javadriver.MongoObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class JFMongoObject
{
  private static Logger LOGGER = Logger.getLogger(JFMongoObject.class.getName());
  
  private JSONObject mObj;
  private String colname;
  
  public JFMongoObject(String collection)
  {
    this.colname = collection;
    this.mObj = new JSONObject();
  }
  
  public JFMongoObject(String collection, JSONObject payload)
  {
    this.colname = collection;
    this.mObj = payload;
  }
  
  public JFMongoObject(String collection, MongoObject obj)
  {
    this.colname = collection;
    mObj = obj.toJson();
  }
  
  public JSONObject toJson()
  {
    return mObj;
  }
  
  public String getCollectionName()
  {
    return this.colname;
  }
  
  public String getObjectId()
  {
    return mObj.getString(Constants.Props.objectId.toString());
  }
  
  public Object get(String key)
  {
    return mObj.get(key);
  }
  
  public Object opt(String key)
  {
    return mObj.opt(key);
  }
  
  public ContainerObjectPointer[] getPointer(String key)
  {
    List<ContainerObjectPointer> pointerList = new ArrayList<ContainerObjectPointer>(
        0);
    Object o = mObj.opt(key);
    if (o instanceof JSONObject)
    {
      ContainerObject typedObj = ContainerObjectFactory.getChildObject(key,
          (JSONObject) o);
      if (typedObj instanceof ContainerObjectPointer)
      {
        ContainerObjectPointer pointer = (ContainerObjectPointer) typedObj;
        pointerList.add(pointer);
      }
    } else if (o instanceof JSONArray)
    {
      JSONArray a = (JSONArray) o;
      for (int i = 0; i < a.length(); i++)
      {
        JSONObject element = a.getJSONObject(i);
        if (element != null)
        {
          ContainerObject typedObj = ContainerObjectFactory.getChildObject(key, (JSONObject) element);
          if (typedObj instanceof ContainerObjectPointer)
          {
            ContainerObjectPointer pointer = (ContainerObjectPointer) typedObj;
            pointerList.add(pointer);
          }
        } 
        else
        {
          LOGGER.log(Level.INFO, "The key " + key
              + " value is JSONArray but the element is not JSONObject."
              + element);
        }
      }
    } 
    else
    {
      LOGGER.log(Level.INFO, "The key " + key + " value is " + o
          + " is not JSONObject, ignore.");
    }
    
    ContainerObjectPointer[] pointers = new ContainerObjectPointer[pointerList
        .size()];
    pointers = pointerList.toArray(pointers);
    return pointers;
  }
}
