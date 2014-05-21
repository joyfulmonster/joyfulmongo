package com.joyfulmongo.db;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;

public class JFMongoUser extends JFMongoObject
{
  public static final String S_USER_COLLNAME = "_User";
  
  public JFMongoUser()
  {
    super(S_USER_COLLNAME);
  }
  
  public JFMongoUser(JSONObject json)
  {
    super(S_USER_COLLNAME, json);
  }
  
  public static JFMongoCmdQuery.Builder getQuery()
  {
    JFMongoCmdQuery.Builder builder = new JFMongoCmdQuery.Builder(
        S_USER_COLLNAME).projectionExclude(Constants.Props.password.toString());
    return builder;
  }
  
  public static String generateSessionToken()
  {
    String sessionToken = RandomStringUtils.randomAlphanumeric(25);
    return sessionToken;
  }
}
