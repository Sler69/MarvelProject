package MarvelProject.ConnectionUtils;


import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.ReadConcern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoConnection {
    static Logger logger = LoggerFactory.getLogger(MongoConnection.class);

    public static Integer insertManyCharacters(List<Document> charactersList){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        MongoCollection<Document> characterCollection = database.getCollection("characters").withReadConcern(ReadConcern.LOCAL)
                                                      .withWriteConcern(WriteConcern.ACKNOWLEDGED);
        try {
            characterCollection.insertMany(charactersList);
        } catch (Exception e){
            logger.error(e.toString());
        }finally {
            mongoClient.close();
        }
        return 1;
    }

    public static Integer insertArrayIntoCharacter(List<Document> arrayToInsert, int characterId, String key){
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
        } catch (Exception e){
            logger.error(e.toString());
        }finally {
            mongoClient.close();
        }
        return 1;
    }

    public static Document getCharacter(String name){
        Document character;
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        MongoCollection<Document> characterCollection = database.getCollection("characters").withReadConcern(ReadConcern.LOCAL)
                .withWriteConcern(WriteConcern.ACKNOWLEDGED);
        character = (Document)  characterCollection.find(new Document("name", name)).first();
        if(character == null){
            logger.warn("There was no character with the name: " + name);
        }
        mongoClient.close();
        return character;
    }

    public static MongoCursor<Document> getCharactersInteracting(List<Integer> comicsIds ){
        MongoClient mongoClient =  MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("marvel");
        MongoCollection<Document> characterCollection = database.getCollection("characters").withReadConcern(ReadConcern.LOCAL)
                .withWriteConcern(WriteConcern.ACKNOWLEDGED);
        Document query = new Document("comics.id", new Document("$in", comicsIds));

        return characterCollection.find(query).iterator();
    }

}
