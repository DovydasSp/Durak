package Server;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DB {
    private static MongoDatabase mongoDB;

    synchronized public static MongoDatabase Instance(){
        if(mongoDB == null){
            MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1:27017");
            CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoDB = mongoClient.getDatabase("test").withCodecRegistry(pojoCodecRegistry);
        }
        return mongoDB;
    }

    public static boolean collectionExists(final String collectionName) {
        MongoIterable<String> collectionNames = Instance().listCollectionNames();
        for (final String name : collectionNames) {
            if (name.equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }
}
