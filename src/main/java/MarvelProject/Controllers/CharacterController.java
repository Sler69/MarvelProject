package MarvelProject.Controllers;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CharacterController {

    public static JsonObject getCharacterCollaborators(){

        JsonResponseModel responseForCharacters = MarvelAPIRequests.getCharactersRequest("Iron Man");
        JsonObject informationService = responseForCharacters.getData();
        JsonArray characters = informationService.getAsJsonArray("results");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CharacterRawDTO>>() {}.getType();
        List<CharacterRawDTO> listCharacters = gson.fromJson(characters.toString(), listType);
        listCharacters.forEach(character -> {

        });
        return new JsonObject();
    }


}
