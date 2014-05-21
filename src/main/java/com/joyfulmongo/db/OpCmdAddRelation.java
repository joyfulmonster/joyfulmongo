package com.joyfulmongo.db;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpCmdAddRelation extends OpCmd
{
  public enum AddRelProps
  {
    objects,
  }
  
  public OpCmdAddRelation(String key, JSONObject json)
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
        
        insertRelation(classname, objId, key, pointer);
        upsertRelationMetadata(classname, key, pointerClassname);
      }
    }
    
    if (pointerClassname != null)
    {
      ContainerObjectRelation prel = new ContainerObjectRelation(
          pointerClassname);
      parseObject.put(key, prel.toJson());
    }
  }
  
  private void upsertRelationMetadata(String classname, String key2,
      String pointerClassname)
  {
    JFMongoRelationshipMetadata relation = new JFMongoRelationshipMetadata();
    relation.setPointerClassName(pointerClassname);
    relation.setRelKey(key);
    relation.setRelClassName(classname);
    
    JSONObject query = new JSONObject();
    query.put(JFMongoRelationshipMetadata.Props.relKey.toString(), key);
    query.put(JFMongoRelationshipMetadata.Props.relClassName.toString(),
        classname);
    
    JFMongoCmdUpsert createCmd = new JFMongoCmdUpsert(
        JFMongoRelationshipMetadata.S_METADATA_COLLNAME, query,
        relation.toJson());
    createCmd.invoke();
  }
  
  private void insertRelation(String classname, String objId, String key2,
      ContainerObjectPointer pointer)
  {
    JFMongoRelationship relation = new JFMongoRelationship();
    relation.setPointerClassName(pointer.getClassName());
    relation.setPointerObjectId(pointer.getObjectId());
    relation.setRelKey(key);
    relation.setRelClassName(classname);
    relation.setRelObjectId(objId);
    
    JFMongoCmdCreate createCmd = new JFMongoCmdCreate(relation);
    createCmd.invoke();
  }

  @Override
  public void onQuery(String collectionName, JSONObject parseObject)
  {
    // TODO Auto-generated method stub
    
  }
}
