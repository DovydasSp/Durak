package Server;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DB {
    private static MongoDatabase mongoDB;

    synchronized public static MongoDatabase Instance(){
        if(mongoDB == null){
            MongoClient mongoClient = MongoClients.create("mongodb+srv://durak:durak@durakdb-b1ola.azure.mongodb.net/test?retryWrites=true&w=majority");
            CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoDB = mongoClient.getDatabase("Durak").withCodecRegistry(pojoCodecRegistry);
        }
        return mongoDB;
    }
}
