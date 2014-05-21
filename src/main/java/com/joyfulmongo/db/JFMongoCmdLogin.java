package com.joyfulmongo.db;

import java.util.List;

import com.joyfulmongo.controller.JFCConstants;
import com.joyfulmongo.controller.JFUserError;
import com.mongodb.util.Util;

public class JFMongoCmdLogin extends JFMongoCmd
{
  private String username;
  private String password;
  
  public JFMongoCmdLogin(String username, String password)
  {
    this.username = username;
    this.password = password;
  }
  
  @Override
  protected void beforeExecute()
  {
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    String md5Password = Util.hexMD5(password.getBytes());
    
    System.out.println ("Password " + password + " md5=" + md5Password);
    
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(
        JFMongoUser.S_USER_COLLNAME)
        .projectionExclude(Constants.Props.password.toString())
        .whereEquals(Constants.Props.username.toString(), username)
        .whereEquals(Constants.Props.password.toString(), md5Password).build();
    List<JFMongoObject> parseObjs = query.find();
    
    JFMongoCmdResult result = new JFMongoCmdResult();
    if (parseObjs.size() > 0)
    {
      result.put(JFCConstants.Props.data.toString(), parseObjs.get(0));
    } else
    {
      JFUserError e = new JFUserError(JFMongoException.VALIDATION_ERROR, "invalid username and password combination");
      throw e;
    }
    
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return executeResult;
  }
}
