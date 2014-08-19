package com.joyfulmongo.db.javadriver.client;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public interface JFMongoClient {
    /**
     * MongoClient
     *
     * @return
     */
    MongoClient getMongoClient();

    /**
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
