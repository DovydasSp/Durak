package Server;

import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;

public class DB {
    private static com.mongodb.DB mongoDB;

    synchronized public static com.mongodb.DB Instance(){
        if(mongoDB == null){
            MongoClientURI mongoClientURI = new MongoClientURI(
                    "mongodb://durak:durak@durakdb-shard-00-00-b1ola.azure.mongodb.net:27017,durakdb-shard-00-01-b1ola.azure.mongodb.net:27017,durakdb-shard-00-02-b1ola.azure.mongodb.net:27017/test?ssl=true&replicaSet=DurakDB-shard-0&authSource=admin&retryWrites=true&w=majority");
            try{
                MongoClient mongoClient = new MongoClient(mongoClientURI);
                mongoDB = mongoClient.getDB("test");
            } catch (java.net.UnknownHostException ex){

            }
        }
        return  mongoDB;
    }
}
