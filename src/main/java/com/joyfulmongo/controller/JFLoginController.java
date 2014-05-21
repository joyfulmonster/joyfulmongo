package com.joyfulmongo.controller;

import com.joyfulmongo.db.JFMongoCmdLogin;
import com.joyfulmongo.db.JFMongoCmdResult;
import com.joyfulmongo.db.JFMongoObject;
import com.joyfulmongo.db.JFMongoUser;

public class JFLoginController extends
    JFController<JFLoginInput, JFLoginOutput>
{
  @Override
  public JFLoginOutput process(JFLoginInput input)
  {
    String username = input.getUsername();
    String password = input.getPassword();
        
    JFMongoCmdLogin cmd = new JFMongoCmdLogin(username, password);
    
    JFMongoCmdResult commandResult = cmd.invoke();
    
    JFLoginOutput result;
    if (commandResult == null)
    {
      result = new JFLoginOutput();
    } else
    {
      String sessionToken = JFMongoUser.generateSessionToken();
      JFMongoObject obj = (JFMongoObject) commandResult
          .get(JFCConstants.Props.data.toString());
      result = new JFLoginOutput(sessionToken, obj);
    }
    
    return result;
  }
  
}
