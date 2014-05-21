package com.joyfulmonster.parse.test;

import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONObject;

import com.joyfulmongo.db.ContainerObject;
import com.joyfulmongo.db.ContainerObjectDefault;
import com.joyfulmongo.db.ContainerObjectFactory;
import com.joyfulmongo.webservice.UpgradeTool;

import junit.framework.TestCase;

public class UpdateDateTest extends TestCase
{
  private void convertConstraints(String jsonkey, JSONObject json)
  {
    System.out.println("convert contraints " + jsonkey);
    Iterator<String> keys = json.keys();
    while (keys.hasNext())
    {
      String key = keys.next();
      
      Object childObj = json.get(key);
      System.out.println( "  key=" + key  + "=" + childObj);
      if (childObj instanceof JSONObject)
      {
        ContainerObject cobj = ContainerObjectFactory.getChildObject(key, childObj);
        System.out.println( "  get containerObj=" + cobj);
        if (cobj != null)
        {
          if (cobj instanceof ContainerObjectDefault)
          {
            System.out.println( "  convert sub " + key);
            JSONObject childJson = json.getJSONObject(key);
            convertConstraints(key, childJson);          
          }
          else
          {
            System.out.println ("  onQuery " + key);
            cobj.onQuery(key, json);
          }
        }
        else
        {
          convertConstraints(key, (JSONObject) childObj);
        }        
      }
    }
    
    System.out.println ("Converted Result " + json);
  }

  public void testInput()
  {
    String input = 
        "{\"WR\":{\"$in\":[\"P\"]},\"WDS.B\":\"NgDmSwFLPG\",\"createdAt\":{\"$lt\":{\"__type\":\"Date\",\"iso\":\"2014-01-16T06:01:08.806Z\"}, \"$gt\":{\"__type\":\"Date\",\"iso\":\"2013-01-16T06:01:08.806Z\"}},\"AO\":{\"$in\":[\"S2D\",\"S2L\",\"L2S\"]}}";

    JSONObject json = new JSONObject(input);
    System.out.println ("input " + json);
    
    convertConstraints(null,  json);
    
    System.out.println ("after convert " + json);    
  }
  
  public void testUpdateDate()
  {
    UpgradeTool tool = new UpgradeTool();
    try
    {
      tool.changeDate("");
    } catch (ParseException e)
    {
      e.printStackTrace();
    }
  }
}
