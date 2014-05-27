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

package com.joyfulmongo.db.javadriver;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joyfulmongo.db.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JFDBUtil
{
  private static Logger LOGGER = Logger.getLogger(JFDBUtil.class.getName());
  
  public static DBObject toDBObject(JSONArray a)
  {
    BasicDBList result = new BasicDBList();
    try
    {
      for (int i = 0; i < a.length(); ++i)
      {
        Object o = a.get(i);
        if (o instanceof JSONObject)
        {
          result.add(toDBObject((JSONObject) o));
        } else if (o instanceof JSONArray)
        {
          result.add(toDBObject((JSONArray) o));
        } else
        {
          result.add(o);
        }
      }
      return result;
    } catch (JSONException je)
    {
      throw new IllegalArgumentException(
          "Failed to pass the income JSONArray to DBObject" + a, je);
    }
  }
  
  public static DBObject toDBObject(JSONObject o)
  {
    BasicDBObject result = new BasicDBObject();
    try
    {
      Iterator<String> i = o.keys();
      while (i.hasNext())
      {
        String k = (String) i.next();
        Object v = o.get(k);
        if (v instanceof JSONArray)
        {
          result.put(k, toDBObject((JSONArray) v));
        } else if (v instanceof JSONObject)
        {
          result.put(k, toDBObject((JSONObject) v));
        } else
        {
          result.put(k, v);
        }
      }
      return result;
    } catch (JSONException je)
    {
      throw new IllegalArgumentException(
          "Failed to pass the income jsonObject to DBObject" + o, je);
    }
  }
  
  public static JSONObject toJSONObject(DBObject o)
  {
    JSONObject result = new JSONObject();
    
    try
    {
      Iterator<String> i = o.keySet().iterator();
      while (i.hasNext())
      {
        String k = (String) i.next();
        Object v = o.get(k);
        if (LOGGER.isLoggable(Level.FINE))
        {
          LOGGER.log(Level.FINE, "toJSON Key=[" + k + "]=[" + v + "] " + v.getClass());
        }
        
        if (v instanceof BasicDBList)
        {
          result.put(k, toJSONArray((BasicDBList) v));
        } 
        else if (v instanceof DBObject)
        {
          DBObject dv = (DBObject) v;
          String op = (String) dv.get("__op");
          if (op != null)
          {
            Object objs = dv.get("objects");
            if (objs == null)
            {
              // ignore
            } else if (objs instanceof BasicDBList)
            {
              JSONArray jarray = toJSONArray((BasicDBList) objs);
              result.put(k, jarray);
            } else
            {
              result.put(k, toJSONObject((DBObject) objs));
            }
          } else
          {
            result.put(k, toJSONObject((DBObject) v));
          }
        } 
        else if (v instanceof ObjectId)
        {
          // ignore the mongo objectId;
        } 
        else if (v instanceof Date)
        {
          DateFormat format = Utils.getParseDateFormat();
          result.put(k, format.format((Date)v));
        }
        else
        {
          result.put(k, v);
        }
      }
      return result;
    } catch (JSONException je)
    {
      return null;
    }
  }
  
  public static JSONArray toJSONArray(BasicDBList list)
  {
    JSONArray array = new JSONArray();
    
    for (int i = 0; i < list.size(); i++)
    {
      Object o = list.get(i);
      if (o instanceof BasicDBList)
      {
        JSONArray subarray = toJSONArray((BasicDBList) o);
        array.put(subarray);
      } 
      else if (o instanceof DBObject)
      {
        JSONObject json = toJSONObject((DBObject) o);
        array.put(json);
      }
      else 
      {
        array.put(o);
      }
    }
    
    return array;
  }
}