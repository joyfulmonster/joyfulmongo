package com.joyfulmongo.db;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.json.JSONObject;

public class JFMongoRelationshipMetadata extends JFMongoObject
{
  public static final String S_METADATA_COLLNAME = "_RelationshipMeta";
  private static Logger LOGGER = Logger.getLogger(JFMongoRelationshipMetadata.class.getName());
  
  public JFMongoRelationshipMetadata()
  {
    super(S_METADATA_COLLNAME);
  }
  
  public enum Props
  {
    relClassName, relKey, pointerClassName,
  }
  
  public String getRelClassName()
  {
    return toJson().getString(Props.relClassName.toString());
  }
  
  public void setRelClassName(String relClassName)
  {
    toJson().put(Props.relClassName.toString(), relClassName);
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
  
  private static ConcurrentHashMap<String, String> relationMetadataMap;
  static
  {
    relationMetadataMap = new ConcurrentHashMap<String, String>(); 
  }
  
  public static ContainerObjectRelation queryRelations(String relClass, String relKey)
  {
    String hashKey = relClass + ":" + relKey;
    String pointerClassName = relationMetadataMap.get(hashKey);
    
    if (pointerClassName == null)
    {    
      JFMongoCmdQuery.Builder builder = new JFMongoCmdQuery.Builder(S_METADATA_COLLNAME);
      builder.whereEquals(Props.relClassName.toString(), relClass);
      builder.whereEquals(Props.relKey.toString(), relKey);
      JFMongoCmdQuery query = builder.build();
      
      List<JFMongoObject> objs = query.find();
      if (objs.size() > 1)
      {
        LOGGER.warning("Found more than one relationMetadataMapping for " + hashKey);
      }
      if (objs.size() == 0)
      {
        LOGGER.severe("Could not find relationMetadataMapping for " + hashKey);
      }
      else
      {
        JFMongoObject obj = objs.get(0);
        JSONObject payload = obj.toJson();
        pointerClassName = payload.getString(Props.pointerClassName.toString());
        relationMetadataMap.put(hashKey, pointerClassName);
      }
    }

    ContainerObjectRelation result = new ContainerObjectRelation(pointerClassName);
    return result;
  }
}
