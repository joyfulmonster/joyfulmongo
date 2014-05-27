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

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryConditionFilterGeoQuery implements QueryConditionFilter
{
  public static final String S_KEY = "$nearSphere";
  
  public static void adjustConstraints(JSONObject constraints)
  {
    String geoPointKey = null;
    ContainerObjectGeoPoint geoPoint = null;
    
    Iterator<String> iter = constraints.keys();
    while (iter.hasNext())
    {
      String constraintKey = iter.next();
      Object o = constraints.get(constraintKey);
      if (o instanceof JSONObject)
      {
        Object nearSphere = ((JSONObject) o).opt(S_KEY);
        if (nearSphere != null && nearSphere instanceof JSONObject)
        {
          ContainerObject childObj = ContainerObjectFactory.getChildObject(
              S_KEY, (JSONObject) nearSphere);
          if (childObj instanceof ContainerObjectGeoPoint)
          {
            geoPointKey = constraintKey;
            geoPoint = (ContainerObjectGeoPoint) childObj;
            break;
          }
        }
      }
    }
    
    if (geoPointKey != null && geoPoint != null)
    {
      constraints.remove(geoPointKey);
      JSONArray geoArray = geoPoint.to2DArray();
      JSONObject nearSphereCondition = new JSONObject();
      nearSphereCondition.put(S_KEY, geoArray);
      constraints.put(ContainerObjectGeoPoint.S_GEO_POINT, nearSphereCondition);
    }
  }
}
