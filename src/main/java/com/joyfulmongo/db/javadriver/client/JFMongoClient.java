package com.joyfulmongo.db.javadriver.client;

import com.mongodb.MongoClient;
import com.mongodb.DB;

public interface JFMongoClient
{
  /**
   * MongoClient
   * 
   * @return
   */
  MongoClient getMongoClient();
  
  /**
   * 
   * @return
   */
  String getDBName();

  
  /**
   * Get the database.
   * 
   * @return
   */
  DB getMongoDB();
}
