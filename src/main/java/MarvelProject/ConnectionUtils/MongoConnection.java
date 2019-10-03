package MarvelProject.ConnectionUtils;


import MarvelProject.Controllers.CharacterController;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ReadConcern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoConnection {
    static Logger logger = LoggerFactory.getLogger(MongoConnection.class);

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
            logger.error(e.toString());
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
            logger.error(e.toString());
        }finally {
            mongoClient.close();
        }
        return 1;
    }

}
