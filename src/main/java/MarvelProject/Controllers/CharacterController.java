package MarvelProject.Controllers;

import MarvelProject.ConnectionUtils.MongoConnection;
import MarvelProject.DAO.CharactersDAO;
import MarvelProject.DTO.CharacterInteractionDTO;
import MarvelProject.DTO.CharacterMongoDTO;
import MarvelProject.DTO.ComicRawDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterController {
    private static Logger logger = LoggerFactory.getLogger(CharacterController.class);

    public static JsonObject getCharacterInteraction(Request request, Response response){

        String characterName = request.params(":character").replaceAll("_"," ");
        Document characterDatabase = MongoConnection.getCharacter(characterName);
        JsonObject objectResponse = new JsonObject();

        if(characterDatabase == null){
            logger.warn("We dont have the character: " + characterName + " on our database");
            response.status(404);
            objectResponse.addProperty("error", "Character not found ");
            return objectResponse;
        }

        String jsonString = characterDatabase.toJson();
        Gson gson = new Gson();
        CharacterMongoDTO character = gson.fromJson(jsonString, CharacterMongoDTO.class);
        List<Integer> comicsIds = new ArrayList<>();
        Map<Integer, ComicRawDTO> characterComics = new HashMap<>();
        character.getComics().forEach(comic -> {
            Integer comicId = comic.getId();
            comicsIds.add(comicId);
            characterComics.put(comicId,comic);
        });

        if(comicsIds.isEmpty()){
            logger.info("The comics for the user was empty we cannot find interaction with empty array");
            response.status(404);
            objectResponse.addProperty("error", "Couldn't find the comics of the character ");
            return objectResponse;
        }

        List<CharacterInteractionDTO> listCharacterInteracted = new ArrayList<>();
        try (MongoCursor<Document> characterShareComics = MongoConnection.getCharactersInteracting(comicsIds)) {

            if(characterComics.isEmpty()){
                logger.warn("There is no interaction between the character:" + characterName + " and the characters in the database");
                response.status(404);
                objectResponse.addProperty("error", "No interaction between the character and the characters in database ");
                return objectResponse;
            }

            while (characterShareComics.hasNext()) {
                CharacterMongoDTO characterApperance = gson.fromJson(characterShareComics.next().toJson(), CharacterMongoDTO.class);
                 if(characterApperance.get_id() != character.get_id()) {
                     String characterApperanceName = characterApperance.getName();
                     ArrayList<String> comicsAppearance = new ArrayList<String>();
                     characterApperance.getComics().forEach(comic -> {
                        if(characterComics.containsKey(comic.getId())){
                            comicsAppearance.add(comic.getTitle());
                        }
                     });
                     CharacterInteractionDTO responseCharacter = new CharacterInteractionDTO(characterApperanceName, comicsAppearance);
                     listCharacterInteracted.add(responseCharacter);
                 }
            }
        }

        JsonElement elementCharactersResponse = gson.toJsonTree(listCharacterInteracted, new TypeToken<List<CharacterInteractionDTO>>() {}.getType());
        JsonArray arrayCharactersResponse = elementCharactersResponse.getAsJsonArray();
        objectResponse.add("characters", arrayCharactersResponse );
        objectResponse.addProperty("last_sync", character.getLast_sync());
        return objectResponse;
    }
}
