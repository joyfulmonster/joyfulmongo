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

import org.json.JSONObject;

public class ContainerObjectFactory
{
  public enum TypeProps
  {
    __type, __op,
  }
  
  public enum OpType
  {
    Add, AddRelation, RemoveRelation, Increment, other;
    
    public static OpType getEnum(String str)
    {
      OpType result = other;
      OpType[] types = OpType.values();
      for (OpType type : types)
      {
        if (type.toString().equals(str))
        {
          result = type;
          break;
        }
      }
      
      return result;
    }
  }
  
  public enum TypeType
  {
    Pointer, Object, GeoPoint, Relation, Date, other;
    
    public static TypeType getEnum(String str)
    {
      TypeType result = other;
      TypeType[] types = TypeType.values();
      for (TypeType type : types)
      {
        if (type.toString().equals(str))
        {
          result = type;
          break;
        }
      }
      
      return result;
    }
  }
  
  public static ContainerObject getChildObject(String key, Object json)
  {
    ContainerObject result = null;
    if (json instanceof JSONObject)
    {
      JSONObject childJson = (JSONObject) json;
      String type = childJson.optString(TypeProps.__type.toString());
      if (type == null || type.length() == 0)
      {
        String optype = childJson.optString(TypeProps.__op.toString());
        if (optype == null || optype.length() == 0)
        {
          result = null;
        } 
        else
        {
          result = getOpCmd(key, optype, childJson);
        }
      } 
      else
      {
        result = getTypedObject(key, type, childJson);
      }
    }
    
    return result;
  }
  
  private static OpCmd getOpCmd(String key, String type, JSONObject json)
  {
    OpType optype = OpType.getEnum(type);
    OpCmd result = null;
    switch (optype)
    {
      case Add:
        result = new OpCmdAdd(key, json);
        break;
      case AddRelation:
        result = new OpCmdAddRelation(key, json);
        break;
      case RemoveRelation:
        result = new OpCmdRemoveRelation(key, json);
        break;
      case Increment:
        result = new OpCmdIncrease(key, json);
        break;
      case other:
      default:
    }
    
    return result;
  }
  
  private static ContainerObject getTypedObject(String key, String type,
      JSONObject json)
  {
    TypeType optype = TypeType.getEnum(type);
    ContainerObject result;
    switch (optype)
    {
      case Pointer:
        result = new ContainerObjectPointer(key, json);
        break;
      case Object:
        result = new ContainerObjectPointerObject(key, json);
        break;
      case GeoPoint:
        result = new ContainerObjectGeoPoint(key, json);
        break;
      case Relation:
        result = new ContainerObjectRelation(key, json);
        break;
      case Date:
        result = new ContainerObjectDate(key, json);
        break;
      case other:
      default:
        result = new ContainerObjectDefault(key, json);
        break;
    }
    
    return result;
  }
}
