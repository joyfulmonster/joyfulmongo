package com.joyfulmongo.db;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.joyfulmongo.controller.JSONObjectSupport;

public class ContainerObjectDate extends JSONObjectSupport implements
  ContainerObject
{
  private static Logger LOGGER = Logger.getLogger(ContainerObjectDate.class.getName());
  private String key;
  public ContainerObjectDate(String key, JSONObject json)
  {
    super(json);    
    this.key = key;
  }

  @Override
  public void onCreate(String collectionName, JSONObject parseObject)
  {
  }

  @Override
  public void onUpdate(String collectionName, JSONObject parseObject)
  {    
  }
  
  @Override
  public void onQuery(String collectionName, JSONObject theChild)
  {     
    JSONObject childJson = theChild.getJSONObject(key);
    
    String iso = childJson.getString("iso");

    try
    {
      Date date = Utils.getParseDateFormat().parse(iso);      
      theChild.put(key, date);    
    } catch (ParseException e)
    {
      // do nothing
      LOGGER.log(Level.WARNING, "Wrong format of date string, skip convert.");
    }
    
  }  
}
