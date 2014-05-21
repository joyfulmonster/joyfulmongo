package com.joyfulmongo.db;

import java.util.List;

import org.json.JSONObject;

import com.joyfulmongo.controller.JFUserError;
import com.mongodb.util.Util;

public class JFMongoCmdSignup extends JFMongoCmdCreate
{
  private String username;
  private String password;
  
  public JFMongoCmdSignup(JSONObject json, String username, String password)
  {
    super(JFMongoUser.S_USER_COLLNAME, json);
    this.username = username;
    this.password = password;
  }
  
  @Override
  protected void beforeExecute()
  {
    super.beforeExecute();
  }
  
  @Override
  protected JFMongoCmdResult execute()
  {
    JFMongoCmdResult result;
    boolean usernameOkay = verifyUsernameUnique(username);
    if (usernameOkay)
    {
      System.out.println ("The password is =" + password);
      String md5Password = Util.hexMD5(password.getBytes());
      getPayload().put(Constants.Props.password.toString(), md5Password);
      result = super.execute();
    } 
    else
    {
      throw new JFUserError(202, "username " + username + " already taken");
    }
    
    return result;
  }
  
  @Override
  protected JFMongoCmdResult afterExecute(JFMongoCmdResult executeResult)
  {
    return super.afterExecute(executeResult);
  }
  
  protected boolean verifyUsernameUnique(String username)
  {
    JFMongoCmdQuery query = new JFMongoCmdQuery.Builder(
        JFMongoUser.S_USER_COLLNAME)
        .projection(Constants.Props.objectId.toString())
        .whereEquals(Constants.Props.username.toString(), username).build();
    List<JFMongoObject> results = query.find();
    return results.size() == 0;
  }
}