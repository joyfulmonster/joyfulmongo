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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpCmdRemoveRelation extends OpCmd
{
  public enum AddRelProps
  {
    objects,
  }
  
  public OpCmdRemoveRelation(String key, JSONObject json)
  {
    super(key, json);
  }
  
  @Override
  public void onCreate(String colname, JSONObject parseObject)
  {
    onUpdate(colname, parseObject);
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parseObject)
  {
    String pointerClassname = null;
    
    JSONArray a = mObj.getJSONArray(AddRelProps.objects.toString());
    for (int i = 0; i < a.length(); i++)
    {
      JSONObject json = a.getJSONObject(i);
      ContainerObject childObj = ContainerObjectFactory.getChildObject(null,
          json);
      if (childObj instanceof ContainerObjectPointer)
      {
        ContainerObjectPointer pointer = (ContainerObjectPointer) childObj;
        pointerClassname = pointer.getClassName();
        
        String classname = colname;
        String objId = parseObject.getString(Constants.Props.objectId
            .toString());
        
        JFMongoCmdQuery.Builder builder = new JFMongoCmdQuery.Builder(
            JFMongoRelationship.S_METADATA_COLLNAME);
        builder.whereEquals(
            JFMongoRelationship.Props.pointerClassName.toString(),
            pointer.getClassName());
        builder.whereEquals(
            JFMongoRelationship.Props.pointerObjectId.toString(),
            pointer.getObjectId());
        builder.whereEquals(JFMongoRelationship.Props.relKey.toString(), key);
        builder.whereEquals(JFMongoRelationship.Props.relClassName.toString(),
            classname);
        builder.whereEquals(JFMongoRelationship.Props.relObjectId.toString(),
            objId);
        JFMongoCmdQuery query = builder.build();
        
        List<JFMongoObject> objs = query.find();
        
        for (JFMongoObject obj : objs)
        {
          JFMongoCmdDelete deleteCmd = new JFMongoCmdDelete(
              obj.getCollectionName(), obj.toJson());
          deleteCmd.invoke();
        }
      }
    }
    
    if (pointerClassname != null)
    {
      ContainerObjectRelation prel = new ContainerObjectRelation(
          pointerClassname);
      parseObject.put(key, prel.toJson());
    }
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    // TODO Auto-generated method stub
    
  }
}
