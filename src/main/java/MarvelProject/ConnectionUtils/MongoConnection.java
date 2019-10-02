package MarvelProject.ConnectionUtils;


import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ReadConcern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.util.List;

public class MongoConnection {
    private static final Logger logger = LogManager.getLogger("MongoConnection");
    public static MongoCollection<Document> getCollectionCharacters(){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        return database.getCollection("characters").withReadConcern(ReadConcern.LOCAL).withWriteConcern(WriteConcern.ACKNOWLEDGED);
    }

    public static int insertManyCharacters(List<Document> charactersList){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        MongoCollection<Document> characterCollection = database.getCollection("characters").withReadConcern(ReadConcern.LOCAL)
                                                      .withWriteConcern(WriteConcern.ACKNOWLEDGED);
        try {
            characterCollection.insertMany(charactersList);
            mongoClient.close();
            return 0;
        } catch (Exception e){
            logger.error(e);
        }finally {
            mongoClient.close();
        }
        return 1;
    }
}
