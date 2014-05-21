package com.joyfulmongo.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class JFResults extends JSONArray
{
  private JFResults()
  {
    super();
  }
  
  public static class JFResultsBuilder
  {
    private JFResults marray;
    
    public JFResultsBuilder()
    {
      marray = new JFResults();
    }
    
    public JFResultsBuilder addData(JSONObject data)
    {
      JSONObject jsonData = new JSONObject();
      jsonData.put(JFCConstants.Props.data.toString(), data);
      marray.put(jsonData);
      return this;
    }
    
    public JFResults build()
    {
      return marray;
    }
  }
}
