package MarvelProject.Controllers;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.DAO.CharactersDAO;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.DTO.CollaboratorRawDTO;
import MarvelProject.DTO.ComicRawDTO;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterController {
    static Logger logger = LoggerFactory.getLogger(CharacterController.class);

    public static JsonObject getCharacterCollaborators(Request request, Response response){

        String characterName = request.params(":character").replaceAll("_"," ");
        Gson gson = new Gson();

        logger.info("Starting request for character:" + characterName);

        JsonResponseModel responseForCharacters = MarvelAPIRequests.getCharactersRequest(characterName);
        JsonObject informationService = responseForCharacters.getData();
        JsonArray characters = informationService.getAsJsonArray("results");

        logger.info("Found Amount of characters with the name:" + characterName + " Size: " + characters.size());

        Type listType = new TypeToken<List<CharacterRawDTO>>() {}.getType();
        List<CharacterRawDTO> listCharacters = gson.fromJson(characters.toString(), listType);
        int statusInsert = CharactersDAO.insertCharacters( listCharacters);

        if(statusInsert != 0 ){
            logger.warn("There was an error when inserting the characters");
        }


        Map<Integer,ArrayList<Integer>> characterComics = new HashMap<Integer,ArrayList<Integer>>();

        listCharacters.forEach(character ->{
            ArrayList<Integer> comicIds = new ArrayList<Integer>();
            int characterId = character.getId();
            JsonResponseModel comicsInfo = MarvelAPIRequests.getComicsFromCharacterRequest(character.getId());
            JsonObject comicsInfoService = comicsInfo.getData();
            JsonArray comics = comicsInfoService.getAsJsonArray("results");

            logger.info("Found Amount of comics with the name:" + character.getName() + " Size: " + comics.size());

            Type comicsType = new TypeToken<List<ComicRawDTO>>() {}.getType();
            List<ComicRawDTO> listComics = gson.fromJson(comics.toString(), comicsType);
            listComics.forEach(comic -> {
                comicIds.add(comic.getId());
            });
            characterComics.put(characterId, comicIds);
            int statusInsertComics = CharactersDAO.insertComicsInCharacter(characterId, listComics);
            if(statusInsertComics == 0){
                logger.warn("There was an error when inserting the comics for character: " + character.getName());
            }
        });



        for(Map.Entry<Integer,ArrayList<Integer>> entry : characterComics.entrySet()){
            HashMap<Integer, CollaboratorRawDTO> uniqueCollaborators = new HashMap<Integer,CollaboratorRawDTO>();
            int characterId = entry.getKey();
            ArrayList<Integer> idComics = entry.getValue();
            idComics.forEach(id -> {
                JsonResponseModel collaboratorsInfo = MarvelAPIRequests.getCollaborators(id);
                JsonObject collaboratorsRawData = collaboratorsInfo.getData();
                JsonArray collaborators = collaboratorsRawData.getAsJsonArray("results");
                Type collaboratorsType = new TypeToken<List<CollaboratorRawDTO>>() {}.getType();
                List<CollaboratorRawDTO> listComics = gson.fromJson(collaborators.toString(), collaboratorsType);
                listComics.forEach(comic -> {
                    if(!uniqueCollaborators.containsKey(comic.getId())){
                        uniqueCollaborators.put(comic.getId(),comic);
                    }
                });
                System.out.println(collaboratorsRawData);
            });
            int statusInsertCollaborators = CharactersDAO.insertCollaboratorsInCharacter(characterId, uniqueCollaborators);
            if(statusInsertCollaborators == 0){
                logger.warn("There was an error inserting the collaborators for character Id" + characterId );
            }
        };


        
        return new JsonObject();
    }


}
