package com.joyfulmongo.controller;

import org.json.JSONObject;

public class JFSignupInput extends JFSingleDataInput
{
  public JFSignupInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public String getUsername()
  {
    JSONObject data = getData();
    return data.getString(JFCConstants.Props.username.toString());
  }
  
  public String getPassword()
  {
    return getString(JFCConstants.Props.user_password.toString());
  }
}
