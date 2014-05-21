package com.joyfulmongo.db.javadriver;

import com.joyfulmongo.db.javadriver.client.JFMongoClient;
import com.joyfulmongo.db.javadriver.client.JFMongoClientFactory;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class JFDBCollectionFactory
{
  private static JFDBCollectionFactory sInstance;
  
  public static JFDBCollectionFactory getInstance()
  {
    if (sInstance == null)
    {
      sInstance = new JFDBCollectionFactory();
    }
    return sInstance;
  }
  
  private DB db;
  
  private JFDBCollectionFactory()
  {
    JFMongoClient client = JFMongoClientFactory.getInstance();
    db = client.getMongoDB();
  }
  
  public JFDBCollection getCollection(String name)
  {
    DBCollection collection = db.getCollection(name);
    
    return new JFDBCollection(name, collection);
  }
}
