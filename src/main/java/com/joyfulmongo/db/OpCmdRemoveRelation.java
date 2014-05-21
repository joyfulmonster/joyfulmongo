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
