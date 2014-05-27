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

import org.json.JSONObject;

public class QueryConditionFilterRelation implements QueryConditionFilter
{
  public static final String S_KEY = "$relatedTo";
  
  private JSONObject relatedTo;
  
  public enum Props
  {
    key, object, objects,
  }
  
  public static boolean isRelationQuery(JSONObject constraints)
  {
    Object obj = constraints.opt(S_KEY);
    return (obj != null);
  }
  
  private JFMongoCmdQuery.Builder origBuilder;
  
  public QueryConditionFilterRelation(JFMongoCmdQuery.Builder builder)
  {
    origBuilder = builder;
  }
  
  public List<String> getRelationObjectIds()
  {
    ContainerObjectPointer referralPointer = getReferalPointer();
    
    JFMongoCmdQuery.Builder newBuilder = new JFMongoCmdQuery.Builder(
        JFMongoRelationship.S_METADATA_COLLNAME);
    newBuilder.whereEquals(JFMongoRelationship.Props.relClassName.toString(),
        referralPointer.getClassName());
    newBuilder.whereEquals(JFMongoRelationship.Props.relObjectId.toString(),
        referralPointer.getObjectId());
    newBuilder.whereEquals(JFMongoRelationship.Props.relKey.toString(),
        getRelationKey());
    
    JFMongoCmdQuery query = newBuilder.build();
    List<JFMongoObject> objs = query.find();
    
    List<String> result = new ArrayList<String>(0);
    for (JFMongoObject obj : objs)
    {
      JSONObject json = obj.toJson();
      String pointedObjId = json
          .getString(JFMongoRelationship.Props.pointerObjectId.toString());
      result.add(pointedObjId);
    }
    return result;
  }
  
  private JSONObject getRelatedTo()
  {
    if (relatedTo == null)
    {
      JSONObject constraints = origBuilder.getConstraints();
      relatedTo = constraints.optJSONObject(S_KEY);
      if (relatedTo == null)
      {
        throw new IllegalArgumentException(
            "The original builder constraint is not a relatedTo constraints");
      }
    }
    return relatedTo;
  }
  
  public String getRelationKey()
  {
    JSONObject json = getRelatedTo();
    return json.getString(Props.key.toString());
  }
  
  public ContainerObjectPointer getReferalPointer()
  {
    JSONObject json = getRelatedTo();
    JSONObject object = json.getJSONObject(Props.object.toString());
    return new ContainerObjectPointer(null, object);
  }
  
  public static void adjustConstraints(JSONObject constraints)
  {
    constraints.remove(S_KEY);
  }
  
  public String getRedirectClassNameForKey(String thekey)
  {
    String result = null;
    ContainerObjectPointer pointer = getReferalPointer();
    JFMongoObject obj = pointer.getRefereeObject();
    Object json = obj.opt(thekey);
    if (json != null)
    {
      ContainerObject childObj = ContainerObjectFactory.getChildObject(thekey,
          json);
      if (childObj instanceof ContainerObjectRelation)
      {
        result = ((ContainerObjectRelation) childObj).getClassname();
      }
    }
    
    return result;
  }
  
  /*
   * public JFMongoCmdQuery.Builder getActualQuery() { JFParsePointer pointer =
   * getPointer(); String relationKey = getRelationKey();
   * 
   * JFMongoObject refereeObj = pointer.getRefereeObject(); JSONObject objJson =
   * refereeObj.toJson();
   * 
   * String collname = origBuilder.getCollection();
   * 
   * RelationMetadata.queryRelations(collname, relationKey);
   * 
   * JSONArray pointerArray = objJson.getJSONArray(relationKey);
   * 
   * String refereeCollection = null; List<String> objectIds = new
   * ArrayList<String>(); for (int i=0; i<pointerArray.length(); i++) {
   * JSONObject onePointerJson = pointerArray.getJSONObject(i); JFParsePointer
   * onePointer = new JFParsePointer(relationKey, onePointerJson);
   * refereeCollection = onePointer.getClassName();
   * objectIds.add(onePointer.getObjectId()); }
   * 
   * JFMongoCmdQuery.Builder newBuilder = new
   * JFMongoCmdQuery.Builder(refereeCollection); JSONObject constraints =
   * origBuilder.getConstraints(); constraints.remove(S_KEY);
   * newBuilder.constraints(constraints);
   * newBuilder.whereContainedIn(JFParseConstants.Props.objectId.toString(),
   * objectIds); newBuilder.projection(origBuilder.getProjections());
   * newBuilder.projectionExclude(origBuilder.getProjectionsExclude());
   * newBuilder.include(origBuilder.getIncludes());
   * newBuilder.limit(origBuilder.getLimit());
   * newBuilder.skip(origBuilder.getSkip());
   * newBuilder.sort(origBuilder.getSort());
   * 
   * return newBuilder; }
   */
}
