package com.joyfulmongo.db.javadriver.client;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.util.logging.Logger;

//import com.baidu.bae.api.util.BaeEnv;

public class BAEMongoClient implements JFMongoClient {
    private static Logger LOGGER = Logger
            .getLogger(JFMongoClient.class.getName());

    /*
     * private static String baeUsername = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
     * private static String baePassword = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
     */
    private static String baeDatabaseName = "GsWZNgpfdpHWPyeTeiSy";

    private static MongoClient mongoClient;

    @Override
    public MongoClient getMongoClient() {
    /*
     * String serverName; //String baeHost =
     * BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_MONGO_IP); //String baePort =
     * BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_MONGO_PORT); //serverName =
     * baeHost + ":" + baePort;
     * 
     * if (mongoClient == null) { try { if (LOGGER.isLoggable(Level.INFO)) {
     * LOGGER.log(Level.INFO, "Connect to server " + serverName); }
     * 
     * mongoClient = new MongoClient(new ServerAddress(serverName),
     * Arrays.asList(MongoCredential.createMongoCRCredential(baeUsername,
     * baeDatabaseName, baePassword.toCharArray())), new
     * MongoClientOptions.Builder().cursorFinalizerEnabled(false).build()); }
     * catch (MongoException e) { e.printStackTrace(); } catch
     * (UnknownHostException e) { e.printStackTrace(); } }
     * 
     * return mongoClient;
     */
        return null;
    }

    @Override
    public String getDBName() {
        return baeDatabaseName;
    }

    @Override
    public DB getMongoDB() {
        MongoClient client = getMongoClient();
        DB mongoDB = client.getDB(getDBName());
        // mongoDB.authenticate(baeUsername, baePassword.toCharArray());
        return mongoDB;
    }
}
