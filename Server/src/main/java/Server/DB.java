package Server;

import com.mongodb.MongoClientURI;

public class DB {
    private static MongoClientURI mongoClient;

    synchronized public static MongoClientURI Instance(){
        if(mongoClient == null)
            mongoClient = new MongoClientURI("mongodb://durakdb:durakdbopp@durakdb-b1ola.azure.mongodb.net/test?retryWrites=true&w=majority");
        return  mongoClient;
    }
}
