package com.joyfulmongo.controller;

import org.json.JSONObject;

public class JFUpdateInput extends JFSingleDataInput
{
  public JFUpdateInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public JFUpdateInput(String colname, JSONObject data)
  {
    super(colname, data);
  }
}
