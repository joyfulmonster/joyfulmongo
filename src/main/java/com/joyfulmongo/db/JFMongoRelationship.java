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

public class JFMongoRelationship extends JFMongoObject
{
  public static final String S_METADATA_COLLNAME = "_RelMeta";
  
  public JFMongoRelationship()
  {
    super(S_METADATA_COLLNAME);
  }
  
  public enum Props
  {
    relClassName, relObjectId, relKey, pointerClassName, pointerObjectId,
  }
  
  public String getRelClassName()
  {
    return toJson().getString(Props.relClassName.toString());
  }
  
  public void setRelClassName(String relClassName)
  {
    toJson().put(Props.relClassName.toString(), relClassName);
  }
  
  public String getRelObjectId()
  {
    return toJson().getString(Props.relObjectId.toString());
  }
  
  public void setRelObjectId(String relObjectId)
  {
    toJson().put(Props.relObjectId.toString(), relObjectId);
  }
  
  public String getRelKey()
  {
    return toJson().getString(Props.relKey.toString());
  }
  
  public void setRelKey(String relKey)
  {
    toJson().put(Props.relKey.toString(), relKey);
  }
  
  public String getPointerClassName()
  {
    return toJson().getString(Props.pointerClassName.toString());
  }
  
  public void setPointerClassName(String pointerClassName)
  {
    toJson().put(Props.pointerClassName.toString(), pointerClassName);
  }
  
  public String getPointerObjectId()
  {
    return toJson().getString(Props.pointerObjectId.toString());
  }
  
  public void setPointerObjectId(String pointerObjectId)
  {
    toJson().put(Props.pointerObjectId.toString(), pointerObjectId);
  }
  
  public static List<String> queryRelations(String relClass,
      String relObjectId, String relKey)
  {
    List<String> pointerObjs = new ArrayList<String>(0);
    JFMongoCmdQuery.Builder builder = new JFMongoCmdQuery.Builder(
        JFMongoRelationship.S_METADATA_COLLNAME);
    builder.whereEquals(JFMongoRelationship.Props.relKey.toString(), relKey);
    builder.whereEquals(JFMongoRelationship.Props.relClassName.toString(),
        relClass);
    builder.whereEquals(JFMongoRelationship.Props.relObjectId.toString(),
        relObjectId);
    JFMongoCmdQuery query = builder.build();
    
    List<JFMongoObject> objs = query.find();
    
    for (JFMongoObject obj : objs)
    {
      pointerObjs.add(obj.getObjectId());
    }
    
    return pointerObjs;
  }
}
