package com.joyfulmongo.controller;

public class JFLoginInput extends JFInput
{
  public JFLoginInput(String jsonStr)
  {
    super(jsonStr);
  }
  
  public String getUsername()
  {
    return getString(JFCConstants.Props.username.toString());
  }
  
  public String getPassword()
  {
    return getString(JFCConstants.Props.user_password.toString());
  }
}
