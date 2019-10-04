package MarvelProject.Controllers;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.ConnectionUtils.MongoConnection;
import MarvelProject.DAO.CharactersDAO;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.DTO.CollaboratorRawDTO;
import MarvelProject.DTO.ComicRawDTO;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

public class CollaboratorController {
    private static Logger logger = LoggerFactory.getLogger(CollaboratorController.class);

    public static JsonObject getCharacterCollaborators(Request request, Response response){

        String characterName = request.params(":character").replaceAll("_"," ");
        logger.info("Consulting the database for character: " + characterName );
        Document characterMongoData = MongoConnection.getCharacter(characterName);

        if(characterMongoData == null){
            logger.info("Didn't find any character on the database, hydrating character from Marvel Api");
           return hydratingDatabaseWithCharacter(characterName);
        }

        String json = characterMongoData.toJson().toString();
        JsonObject characterData = new JsonParser().parse(json).getAsJsonObject();
        JsonObject outputVariable = new JsonObject();
        JsonObject collaboratorPerCharacter = new JsonObject();
        collaboratorPerCharacter.add("collaborators", characterData.get("collaborators"));
        collaboratorPerCharacter.add("last_sync", characterData.get("last_sync"));
        outputVariable.add(characterData.get("_id").toString() , collaboratorPerCharacter);
        return outputVariable;
    }

    private static JsonObject hydratingDatabaseWithCharacter(String characterName){
        Gson gson = new Gson();
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=ts;

        logger.info("Starting request for character:" + characterName);

        // There are characters with the same name therefore considered different characters.
        // AKA heroes that took the mantle of a previous hero. Therefore we could get a result with
        // several characters.
        JsonResponseModel responseForCharacters = MarvelAPIRequests.getCharactersRequest(characterName);
        JsonObject informationService = responseForCharacters.getData();
        JsonArray characters = informationService.getAsJsonArray("results");

        logger.info("Found Amount of characters with the name:" + characterName + " Size: " + characters.size());

        Type listType = new TypeToken<List<CharacterRawDTO>>() {}.getType();
        List<CharacterRawDTO> listCharacters = gson.fromJson(characters.toString(), listType);
        Integer statusInsert = CharactersDAO.insertCharacters( listCharacters , date);

        if(statusInsert == 0){
            logger.warn("There was an error when inserting the characters");
        }


        Map<Integer,ArrayList<Integer>> characterComics = new HashMap<Integer,ArrayList<Integer>>();

        listCharacters.forEach(character ->{
            ArrayList<Integer> comicIds = new ArrayList<Integer>();
            int characterId = character.getId();
            JsonResponseModel comicsInfo = MarvelAPIRequests.getComicsFromCharacterRequest(character.getId());
            JsonObject comicsInfoService = comicsInfo.getData();
            JsonArray comics = comicsInfoService.getAsJsonArray("results");

            logger.info("Found Amount of comics with the character name:" + character.getName() + " Size: " + comics.size());

            Type comicsType = new TypeToken<List<ComicRawDTO>>() {}.getType();
            List<ComicRawDTO> listComics = gson.fromJson(comics.toString(), comicsType);
            listComics.forEach(comic -> {
                comicIds.add(comic.getId());
            });
            characterComics.put(characterId, comicIds);
            Integer statusInsertComics = CharactersDAO.insertComicsInCharacter(characterId, listComics);
            if(statusInsertComics == 0){
                logger.warn("There was an error when inserting the comics for character: " + character.getName());
            }
        });

        Map <Integer, ArrayList<String>> collaboratorsPerCharacter = new HashMap<>();

        for(Map.Entry<Integer,ArrayList<Integer>> entry : characterComics.entrySet()){

            HashMap<Integer, CollaboratorRawDTO> uniqueCollaborators = new HashMap<Integer,CollaboratorRawDTO>();
            int characterId = entry.getKey();
            ArrayList<Integer> idComics = entry.getValue();
            ArrayList<String> sortedListForOutput = new ArrayList<>();

            idComics.forEach(id -> {
                JsonResponseModel collaboratorsInfo = MarvelAPIRequests.getCollaborators(id);
                JsonObject collaboratorsRawData = collaboratorsInfo.getData();
                JsonArray collaborators = collaboratorsRawData.getAsJsonArray("results");
                Type collaboratorsType = new TypeToken<List<CollaboratorRawDTO>>() {}.getType();
                List<CollaboratorRawDTO> listCollaborators = gson.fromJson(collaborators.toString(), collaboratorsType);
                listCollaborators.forEach(collaborator -> {
                    if(!uniqueCollaborators.containsKey(collaborator.getId())){
                        uniqueCollaborators.put(collaborator.getId(),collaborator);
                        sortedListForOutput.add(collaborator.getFullName());
                    }
                });
                logger.info("Getting information from API comic: " + id + " data:" + collaboratorsRawData);
                collaboratorsPerCharacter.put(characterId,sortedListForOutput);
            });
            Integer statusInsertCollaborators = CharactersDAO.insertCollaboratorsInCharacter(characterId, uniqueCollaborators);
            if(statusInsertCollaborators == 0){
                logger.warn("There was an error inserting the collaborators for character Id" + characterId );
            }
        };

        JsonObject outputObject = new JsonObject();

        for(Map.Entry<Integer,ArrayList<String>> entry : collaboratorsPerCharacter.entrySet()){

            Integer characterId = entry.getKey();
            ArrayList<String> collaborators = entry.getValue();
            JsonArray characterData = new JsonParser().parse(gson.toJson(collaborators)).getAsJsonArray();
            JsonObject collaboratorPerCharacter = new JsonObject();
            collaboratorPerCharacter.add("collaborators", characterData);
            collaboratorPerCharacter.addProperty("last_sync", date.toString());
            outputObject.add(characterId.toString(), collaboratorPerCharacter);

        }

        return outputObject;
    }


}
