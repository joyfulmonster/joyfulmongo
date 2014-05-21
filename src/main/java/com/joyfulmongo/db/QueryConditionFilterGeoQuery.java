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
