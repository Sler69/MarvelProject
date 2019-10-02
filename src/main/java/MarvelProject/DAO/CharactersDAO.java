package MarvelProject.DAO;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.ConnectionUtils.MarvelConnection;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CharactersDAO {

    public static final JsonObject getCollaborators() {

        JsonResponseModel responseForCharacters = MarvelAPIRequests.getCharactersRequest("Iron Man");
        JsonObject informationService = responseForCharacters.getData();
        JsonArray characters = informationService.getAsJsonArray("results");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CharacterRawDTO>>() {}.getType();
        List<CharacterRawDTO> listCharacters = gson.fromJson(characters.toString(), listType);
        listCharacters.forEach(charachter -> {
            System.out.println(charachter);
        });
        return responseForCharacters.getData();
    }
}
