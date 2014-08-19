package com.joyfulmongo.db.javadriver.client;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StandaloneDBMongoClient implements JFMongoClient {
    private static Logger LOGGER = Logger.getLogger(JFMongoClient.class.getName());

    private static MongoClient mongoClient;

    @Override
    public MongoClient getMongoClient() {
        String server = System.getProperty(Constants.S_MONGO_HOSTNAME, "localhost");
        String port = System.getProperty(Constants.S_MONGO_PORT, "27017");
        String serverName = server + ":" + port;

        if (mongoClient == null) {
            try {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Connect to server " + serverName);
                }

                mongoClient = new MongoClient(new ServerAddress(serverName));
            } catch (MongoException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return mongoClient;
    }

    @Override
    public String getDBName() {
        return "kcpdb";
    }

    @Override
    public DB getMongoDB() {
        MongoClient client = getMongoClient();
        DB mongoDB = client.getDB(getDBName());
        return mongoDB;
    }
}
