package com.joyfulmongo.db.javadriver.client;

public class JFMongoClientFactory
{  
  private static JFMongoClient sInstance;
  
  public static Boolean isRunningInBAE()
  {  
    String inBAE = System.getProperty("com.joyfulmonster.runningInBAE",  Boolean.FALSE.toString());
    return Boolean.valueOf(inBAE);
  }
  
  public static Boolean isReplicSet()
  {
    String replicSet = System.getProperty(Constants.S_MONGO_REPLICSET);
    return !(replicSet == null || replicSet.length() == 0);
  }
  
  public static JFMongoClient getInstance()
  {
    if (sInstance == null)
    {
      if (isRunningInBAE())
      {
        sInstance = new BAEMongoClient();
      } 
      else
      {
        if (isReplicSet())
        {
          sInstance = new ReplicSetMongoClient();
        }
        else
        {
          sInstance = new StandaloneDBMongoClient();
        }
      }
    }
    
    return sInstance;
  }
}
