package MarvelProject.DAO;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.ConnectionUtils.MarvelConnection;
import MarvelProject.ConnectionUtils.MongoConnection;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class CharactersDAO {

    public static final JsonObject getCollaborators() {

        JsonResponseModel responseForCharacters = MarvelAPIRequests.getCharactersRequest("Iron Man");
        JsonObject informationService = responseForCharacters.getData();
        JsonArray characters = informationService.getAsJsonArray("results");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CharacterRawDTO>>() {}.getType();
        List<CharacterRawDTO> listCharacters = gson.fromJson(characters.toString(), listType);
        CharactersDAO.insertCharacters(listCharacters);
        return responseForCharacters.getData();
    }

    public static void insertCharacters(List<CharacterRawDTO> listCharacters){
        MongoCollection characterCollection = MongoConnection.getCollectionCharacters();
        List<Document> documentsToInsert = new ArrayList<Document>();
        listCharacters.forEach(character -> {
            Document docCharacter = new Document("name", character.getName())
                                        .append("_id", character.getId())
                                        .append("description", character.getDescription())
                                        .append("modified", character.getModified());
            documentsToInsert.add(docCharacter);
        });
        MongoConnection.insertManyCharacters(documentsToInsert);

    }
}
