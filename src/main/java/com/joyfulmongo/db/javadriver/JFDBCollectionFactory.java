/*
 * Copyright 2014 Weifeng Bao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/

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
