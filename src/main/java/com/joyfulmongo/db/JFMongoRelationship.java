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
