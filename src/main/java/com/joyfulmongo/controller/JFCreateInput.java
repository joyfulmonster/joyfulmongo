package com.joyfulmongo.controller;

import org.json.JSONObject;

public class JFCreateInput extends JFSingleDataInput
{
  public JFCreateInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public JFCreateInput(String colname, JSONObject data)
  {
    super(colname, data);
  }
}
