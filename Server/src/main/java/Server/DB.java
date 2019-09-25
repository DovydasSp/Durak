package Server;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DB {
    private static MongoDatabase mongoDB;

    synchronized public static MongoDatabase Instance(){
        if(mongoDB == null){
            MongoClient mongoClient = MongoClients.create("mongodb+srv://durak:durak@durakdb-b1ola.azure.mongodb.net/test?retryWrites=true&w=majority");
            mongoDB = mongoClient.getDatabase("Durak");
        }
        return mongoDB;
    }
}
