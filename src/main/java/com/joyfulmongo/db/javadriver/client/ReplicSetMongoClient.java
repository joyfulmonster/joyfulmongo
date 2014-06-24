package com.joyfulmongo.db.javadriver.client;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class ReplicSetMongoClient implements JFMongoClient
{
  private static Logger LOGGER = Logger.getLogger(JFMongoClient.class.getName());
  
  private static MongoClient mongoClient;
  
  @Override
  public MongoClient getMongoClient()
  {
    String replicSet = System.getProperty(Constants.S_MONGO_REPLICSET);
    
    if (mongoClient == null)
    {
      try
      {
        if (LOGGER.isLoggable(Level.INFO))
        {
          LOGGER.log(Level.INFO, "Connect to replicSet " + replicSet);
        }
        
        List<ServerAddress> servers = getServers(replicSet);
        List<MongoCredential> credentials = getCredentials();
        mongoClient = new MongoClient(servers, credentials);
      } 
      catch (MongoException e)
      {
        e.printStackTrace();
        throw new IllegalStateException("Failed to connect to MongoDB", e);
      } 
    }
    
    return mongoClient;
  }
  
  private List<MongoCredential> getCredentials()
  {
    String bindingName = System.getProperty(Constants.S_MONGO_BINDING_USER);
    String password = System.getProperty(Constants.S_MONGO_BINDING_PWD);
    String dbName = System.getProperty(Constants.S_MONGO_DB, Constants.S_MONGO_DB_DEFAULT);
    String photodbName = System.getProperty(Constants.S_MONGO_PHOTO_DB, Constants.S_MONGO_PHOTO_DB_DEFAULT);
    String smsdbName = System.getProperty(Constants.S_MONGO_SMS_DB, Constants.S_MONGO_SMS_DB_DEFAULT);
    
    MongoCredential credential = MongoCredential.createMongoCRCredential(bindingName, dbName, password.toCharArray());
    MongoCredential photocredential = MongoCredential.createMongoCRCredential(bindingName, photodbName, password.toCharArray());
    MongoCredential smscredential = MongoCredential.createMongoCRCredential(bindingName, smsdbName, password.toCharArray());
    
    List<MongoCredential> credentials = new ArrayList<MongoCredential>(2);
    credentials.add(credential);
    credentials.add(photocredential);
    credentials.add(smscredential);
    return credentials;
  }

  private List<ServerAddress> getServers(String replicSet)
  {
    List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>(1);
    StringTokenizer st = new StringTokenizer(replicSet, ",");
    while (st.hasMoreTokens())
    {
      String inst = st.nextToken();
      StringTokenizer instSt = new StringTokenizer(inst, ":");
      String hostname; 
      int port;
      if (instSt.hasMoreElements())
      {
        hostname = instSt.nextToken();
      }
      else
      {
        throw new IllegalArgumentException ("Empty string is not valid");
      }
      if (instSt.hasMoreElements())
      {
        String portStr = instSt.nextToken();
        port = Integer.parseInt(portStr);
      }
      else
      {
        port = 27017;
      }
      
      try
      {
        ServerAddress addr = new ServerAddress(hostname, port);
        serverAddresses.add(addr);
      } 
      catch (UnknownHostException e)
      {
        throw new IllegalArgumentException (e);
      }
    }
    
    return serverAddresses;
  }
  
  @Override
  public String getDBName()
  {
    String dbName = System.getProperty(Constants.S_MONGO_DB, "kcpdb");
    return dbName;
  }

  @Override
  public DB getMongoDB()
  {    
    MongoClient client = getMongoClient();
    DB mongoDB = client.getDB(getDBName());
    return mongoDB;
  }
}
