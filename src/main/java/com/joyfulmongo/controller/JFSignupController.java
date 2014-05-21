package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdResult;
import com.joyfulmongo.db.JFMongoCmdSignup;
import org.json.JSONObject;

import com.joyfulmongo.db.JFMongoUser;

public class JFSignupController extends
    JFController<JFSignupInput, JFSignupOutput>
{
  @Override
  public JFSignupOutput process(JFSignupInput input)
  {
    JSONObject payload = input.getData();
    
    String username = input.getUsername();
    String password = input.getPassword();
    
    JFMongoCmdSignup cmd = new JFMongoCmdSignup(payload, username, password);
    
    JFMongoCmdResult commandResult = cmd.invoke();
    
    String sessionToken = JFMongoUser.generateSessionToken();
    
    JFSignupOutput result = new JFSignupOutput(sessionToken, commandResult);
    
    return result;
  }
  
}
