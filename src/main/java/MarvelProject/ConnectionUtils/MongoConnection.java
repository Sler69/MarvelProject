package MarvelProject.ConnectionUtils;


import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoConnection {

    public static MongoCollection getCollectionCharacters(){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        return database.getCollection("characters");
    }
}
