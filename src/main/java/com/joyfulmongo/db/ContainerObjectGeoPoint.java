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

import org.json.JSONArray;
import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectGeoPoint extends JSONObjectSupport implements
    ContainerObject
{
  public static String S_GEO_POINT = "__GeoPoint";
  
  public enum Props
  {
    longitude, latitude,
  }
  
  ContainerObjectGeoPoint(String key, JSONObject json)
  {
    super(json);
  }
  
  public Double getLat()
  {
    return mObj.getDouble(Props.latitude.toString());
  }
  
  public Double getLon()
  {
    return mObj.getDouble(Props.longitude.toString());
  }
  
  public JSONArray to2DArray()
  {
    JSONArray a = new JSONArray();
    a.put(getLon());
    a.put(getLat());
    return a;
  }
  
  @Override
  public void onCreate(String colname, JSONObject parentJson)
  {
    onUpdate(colname, parentJson);
  }
  
  @Override
  public void onUpdate(String colname, JSONObject parentJson)
  {
    JSONArray a = to2DArray();
    parentJson.put(S_GEO_POINT, a);
  }

  @Override
  public void onQuery(String collectionName, JSONObject joyObject)
  {    
  }
}
