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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.joyfulmongo.db.javadriver.MongoObject;
import com.joyfulmongo.db.javadriver.MongoQuery;
import org.json.JSONArray;
import org.json.JSONObject;

import com.joyfulmongo.controller.JFCConstants;

public class JFMongoCmdQuery extends JFCommand
{
  private static Logger LOGGER = Logger.getLogger(JFMongoCmdQuery.class.getName());
  
  public static Integer S_DEFAULT_SKIP = 0;
  public static Integer S_DEFAULT_LIMIT = 100;
  
  private MongoQuery query;
  private String collectionName;
  private String[] includeFields;
  private String redirectClassname;
  
  private JFMongoCmdQuery(String colname, MongoQuery dbquery,
                          String redirectClassname)
  {
    this.collectionName = colname;
    this.query = dbquery;
    this.redirectClassname = redirectClassname;
  }
  
  @Override
  protected void beforeExecute()
  {
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    List<JFMongoObject> joyObjects = find();
    JFMongoCmdResult result = new JFMongoCmdResult();
    result.put(JFCConstants.Props.results.toString(), joyObjects);
    if (this.redirectClassname != null)
    {
      result.put(JFCConstants.Props.classname.toString(), redirectClassname);
    }
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return executeResult;
  }
  
  private void setIncludeFields(String... includeFields)
  {
    this.includeFields = includeFields;
  }
  
  public List<JFMongoObject> find()
  {
    List<MongoObject> objs = query.find();
    
    Map<String, ContainerObjectRelation> relationKeyToClassnameMap = getRelationKeyToClassnameMap();
    
    List<JFMongoObject> results = new ArrayList<JFMongoObject>(objs.size());
    
    Map<String, LinkedHashMap<String, List<JFMongoObject>>> includeKeyToParentObjectMap = initPointerMap();
    Map<String, String> includeKeyToPointerColnameMap = new HashMap<String, String>(0);
    
    for (MongoObject obj : objs)
    {
      JFMongoObject joyObject = new JFMongoObject(this.collectionName, obj);
      
      JSONObject json = joyObject.toJson();
      Iterator<String> relKeys = relationKeyToClassnameMap.keySet().iterator();
      while (relKeys.hasNext())
      {
        String relKey = relKeys.next();
        if (!json.has(relKey))
        {
          ContainerObjectRelation rel = relationKeyToClassnameMap.get(relKey);
          json.put(relKey, rel.toJson());
        }
      }
      
      results.add(joyObject);
      linkIncludeObjectIdToParentObject(joyObject, includeKeyToParentObjectMap,
          includeKeyToPointerColnameMap);
    }
    
    processIncludes(includeKeyToParentObjectMap, includeKeyToPointerColnameMap);
    
    return results;
  }
  
  private Map<String, ContainerObjectRelation> getRelationKeyToClassnameMap()
  {
    MongoQuery.Builder distinctRelKeyQuery = new MongoQuery.Builder(JFMongoRelationshipMetadata.S_METADATA_COLLNAME);
    JSONObject constraints = new JSONObject();
    constraints.put(JFMongoRelationshipMetadata.Props.relClassName.toString(), this.collectionName);
    distinctRelKeyQuery.constraints(constraints);
    
    MongoQuery query = distinctRelKeyQuery.build();
    List<MongoObject> objs = query.find();
    
    Map<String, ContainerObjectRelation> result = new HashMap<String, ContainerObjectRelation>(0);
    for (MongoObject obj : objs)
    {
      JSONObject json = obj.toJson();
      String key = json.getString(JFMongoRelationshipMetadata.Props.relKey.toString());
      String pointerClassname = json.getString(JFMongoRelationshipMetadata.Props.pointerClassName.toString());
      ContainerObjectRelation rel = new ContainerObjectRelation(pointerClassname);
      result.put(key, rel);
    }
    
    return result;
  }
  
  private Map<String, LinkedHashMap<String, List<JFMongoObject>>> initPointerMap()
  {
    Map<String, LinkedHashMap<String, List<JFMongoObject>>> pointerMap = new HashMap<String, LinkedHashMap<String, List<JFMongoObject>>>();
    
    if (includeFields != null && includeFields.length > 0)
    {
      for (String includeKey : includeFields)
      {
        LinkedHashMap<String, List<JFMongoObject>> pointers = new LinkedHashMap<String, List<JFMongoObject>>(0);
        pointerMap.put(includeKey, pointers);
      }
    }
    
    return pointerMap;
  }
  
  private void linkIncludeObjectIdToParentObject(JFMongoObject parentObject,
      Map<String, LinkedHashMap<String, List<JFMongoObject>>> pointerMap,
      Map<String, String> includeKeyToPointerColnameMap)
  {
    for (String includeKey : includeFields)
    {
      if (includeKey.length() > 0)
      {        
        ContainerObjectPointer[] pointers = parentObject.getPointer(includeKey);
        for (ContainerObjectPointer pointer : pointers)
        {
          String pointerObjectId = pointer.getObjectId();
          LinkedHashMap<String, List<JFMongoObject>> pointerObjectIdToParentObjectMap = pointerMap
              .get(includeKey);
          List<JFMongoObject> parentObjects = pointerObjectIdToParentObjectMap
              .get(pointerObjectId);
          if (parentObjects == null)
          {
            parentObjects = new ArrayList<JFMongoObject>(1);
            pointerObjectIdToParentObjectMap
                .put(pointerObjectId, parentObjects);
          }
          parentObjects.add(parentObject);
          
          includeKeyToPointerColnameMap.put(includeKey, pointer.getClassName());
        }
      }
    }
  }
  
  private void processIncludes(
      Map<String, LinkedHashMap<String, List<JFMongoObject>>> includeKeyToPointerListMap,
      Map<String, String> includeKeyToPointerColnameMap)
  {
    for (String includeKey : includeFields)
    {
      String pointerColName = includeKeyToPointerColnameMap.get(includeKey);
      if (pointerColName != null)
      {
        LinkedHashMap<String, List<JFMongoObject>> pointerObjectIdToParentObjectsMap = includeKeyToPointerListMap
            .get(includeKey);
        Set<String> referreeObjIds = pointerObjectIdToParentObjectsMap.keySet();
        
        JFMongoCmdQuery.Builder queryBuilder = new JFMongoCmdQuery.Builder(
            pointerColName);
        queryBuilder.whereContainedIn(Constants.Props.objectId.toString(),
            referreeObjIds);
        List<JFMongoObject> refereeObjects = queryBuilder.build().find();
        
        for (JFMongoObject refereeObj : refereeObjects)
        {
          String refereeObjId = refereeObj.getObjectId();
          List<JFMongoObject> parentObjs = pointerObjectIdToParentObjectsMap
              .get(refereeObjId);
          for (JFMongoObject parentObj : parentObjs)
          {
            ContainerObjectPointer[] pointers = parentObj
                .getPointer(includeKey);
            for (ContainerObjectPointer pointer : pointers)
            {
              pointer.replaceObject(refereeObj);
            }
          }
        }
      }
    }
  }
  
  public static class Builder
  {
    private String collectionName;
    private MongoQuery.Builder dbQueryBuilder;
    private JSONObject constraints;
    private List<String> includes;
    private List<String> projections;
    private List<String> projectionsExclude;
    private List<String> sorts;
    private int limit;
    private int skip;
    private String redirectClassNameForKey;
    private String redirectClassName;
    
    public Builder(String collectionName)
    {
      if (collectionName == null || collectionName.length() == 0)
      {
        throw new IllegalArgumentException(
            "Collection name can not be null or zero length");
      }
      
      dbQueryBuilder = new MongoQuery.Builder(collectionName);
      
      this.collectionName = collectionName;
      this.constraints = new JSONObject();
      this.projections = new ArrayList<String>(0);
      this.projectionsExclude = new ArrayList<String>(0);
      this.sorts = new ArrayList<String>(0);
      this.includes = new ArrayList<String>(0);
      this.limit = S_DEFAULT_LIMIT;
      this.skip = S_DEFAULT_SKIP;
      this.redirectClassNameForKey = null;
      this.redirectClassName = null;
    }
    
    public void collection(String colname)
    {
      this.dbQueryBuilder.collection(colname);
      this.collectionName = colname;
    }
    
    public String getCollection()
    {
      return this.collectionName;
    }
    
    public Builder projection(String... fields)
    {
      for (String field : fields)
      {
        projections.add(field);
      }
      return this;
    }
    
    String[] getProjections()
    {
      String[] results = new String[projections.size()];
      results = projections.toArray(results);
      return results;
    }
    
    public Builder projectionExclude(String... fields)
    {
      for (String field : fields)
      {
        projectionsExclude.add(field);
      }
      return this;
    }
    
    String[] getProjectionsExclude()
    {
      String[] results = new String[projectionsExclude.size()];
      results = projectionsExclude.toArray(results);
      return results;
    }
    
    public Builder include(String... fields)
    {
      for (String field : fields)
      {
        includes.add(field);
      }
      return this;
    }
    
    String[] getIncludes()
    {
      String[] results = new String[includes.size()];
      results = includes.toArray(results);
      return results;
    }
    
    public Builder limit(int limit)
    {
      this.limit = limit;
      return this;
    }
    
    int getLimit()
    {
      return this.limit;
    }
    
    public Builder skip(int skip)
    {
      this.skip = skip;
      return this;
    }
    
    int getSkip()
    {
      return this.skip;
    }
    
    public Builder redirectClassNameForKey(String redirectClassNameForKey)
    {
      if (redirectClassNameForKey != null
          && redirectClassNameForKey.length() != 0)
      {
        this.redirectClassNameForKey = redirectClassNameForKey;
      }
      return this;
    }
    
    public Builder sort(String sorts)
    {
      StringTokenizer st = new StringTokenizer(sorts, ",");
      while (st.hasMoreTokens())
      {
        this.sorts.add(st.nextToken());
      }
      return this;
    }
    
    String getSort()
    {
      String result = "";
      if (sorts.size() > 0)
      {
        result = sorts.get(0);
      }
      
      for (int i = 1; i < sorts.size(); i++)
      {
        result += "," + sorts.get(i);
      }
      return result;
    }
    
    public Builder constraints(JSONObject constraints)
    {
      this.constraints = constraints;
      return this;
    }
    
    public Builder whereEquals(String key, String value)
    {
      this.constraints.put(key, value);
      return this;
    }
    
    public void whereContainedIn(String key, Collection<String> objIds)
    {
      JSONArray array = new JSONArray();
      for (String objId : objIds)
      {
        array.put(objId);
      }
      JSONObject inCondition = new JSONObject();
      inCondition.put("$in", array);
      constraints.put(key, inCondition);
    }
    
    JSONObject getConstraints()
    {
      return this.constraints;
    }
    
    public JFMongoCmdQuery build()
    {
      Builder theBuilder = this;
      if (QueryConditionFilterRelation.isRelationQuery(constraints))
      {
        QueryConditionFilterRelation relationCondition = new QueryConditionFilterRelation(
            this);
        List<String> objIds = relationCondition.getRelationObjectIds();
        whereContainedIn(Constants.Props.objectId.toString(), objIds);
        QueryConditionFilterRelation.adjustConstraints(constraints);
        
        if (redirectClassNameForKey != null)
        {
          this.redirectClassName = relationCondition
              .getRedirectClassNameForKey(redirectClassNameForKey);
          if (this.redirectClassName != null)
          {
            collection(this.redirectClassName);
          }
        }
      }
            
      JSONObject effectiveConstraints = theBuilder.getConstraints();
      
      convertConstraints(null, effectiveConstraints);

      QueryConditionFilterGeoQuery.adjustConstraints(effectiveConstraints);
      dbQueryBuilder.constraints(effectiveConstraints);
      dbQueryBuilder.projection(theBuilder.getProjections());
      dbQueryBuilder.projectionExclude(theBuilder.getProjectionsExclude());
      dbQueryBuilder.limit(theBuilder.getLimit());
      dbQueryBuilder.skip(theBuilder.getSkip());
      dbQueryBuilder.sort(theBuilder.getSort());
      
      MongoQuery dbquery = dbQueryBuilder.build();
      JFMongoCmdQuery query = new JFMongoCmdQuery(collectionName, dbquery, redirectClassName);
      query.setIncludeFields(theBuilder.getIncludes());
      return query;
    }
    
    private void convertConstraints(String jsonkey, JSONObject json)
    {
      Iterator<String> keys = json.keys();
      while (keys.hasNext())
      {
        String key = keys.next();
        
        Object childObj = json.get(key);
        if (childObj instanceof JSONObject)
        {
          ContainerObject cobj = ContainerObjectFactory.getChildObject(key, childObj);
          if (cobj != null)
          {
            if (cobj instanceof ContainerObjectDefault)
            {
              JSONObject childJson = json.getJSONObject(key);
              convertConstraints(key, childJson);          
            }
            else
            {
              cobj.onQuery(key, json);
            }
          }
          else
          {
            convertConstraints(key, (JSONObject) childObj);
          }        
        }
      }      
    }
  }
}
