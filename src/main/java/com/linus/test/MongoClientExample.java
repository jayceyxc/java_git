package com.linus.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoClientExample {
    public static void main (String[] args) {
        String host = "127.0.0.1";
        int port = 19191;
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder ();
        MongoClientOptions clientOptions = builder.build ();
        ServerAddress mongoServer = new ServerAddress (host, port);
        MongoClient client = new MongoClient (mongoServer, clientOptions);
        for (String dbName : client.listDatabaseNames ()) {
            System.out.println ("Database Name: " + dbName);
            MongoDatabase database = client.getDatabase (dbName);
            for (String colName : database.listCollectionNames ()) {
                System.out.println ("\tCollection Name: " + colName);
            }
        }
    }
}
