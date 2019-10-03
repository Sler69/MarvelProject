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
import org.bson.conversions.Bson;

import java.util.List;

public class MongoConnection {
    private static final Logger logger = LogManager.getLogger("MongoConnection");

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

    public static int insertArrayIntoCharacter(List<Document> arrayToInsert, int characterId, String key){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        MongoCollection<Document> characterCollection = database.getCollection("characters").withReadConcern(ReadConcern.LOCAL)
                .withWriteConcern(WriteConcern.ACKNOWLEDGED);
        Document find = (Document)  characterCollection.find(new Document("_id", characterId)).first();

        if(find == null){
            logger.error("There was no character with the id:" + characterId + " Cannot insert key: " + key);
            return 0;
        }
        Document updateValue = new Document(key, arrayToInsert);
        Document updateOperation = new Document("$set", updateValue);
        characterCollection.updateOne(find,updateOperation);
        try {
            characterCollection.updateOne(find,updateOperation);
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
