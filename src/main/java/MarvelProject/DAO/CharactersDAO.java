package MarvelProject.DAO;

import MarvelProject.APIRequests.MarvelAPIRequests;
import MarvelProject.ConnectionUtils.MarvelConnection;
import MarvelProject.ConnectionUtils.MongoConnection;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.DTO.CollaboratorRawDTO;
import MarvelProject.DTO.ComicRawDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class CharactersDAO {


    public static int insertCharacters(List<CharacterRawDTO> listCharacters){
        List<Document> documentsToInsert = new ArrayList<Document>();
        listCharacters.forEach(character -> {
            Document docCharacter = new Document("name", character.getName())
                                        .append("_id", character.getId())
                                        .append("description", character.getDescription())
                                        .append("modified", character.getModified());
            documentsToInsert.add(docCharacter);
        });
        return MongoConnection.insertManyCharacters(documentsToInsert);
    }

    public static int insertComicsInCharacter(int id, List<ComicRawDTO> listComics){
        List<Document> comicsToInsert = new ArrayList<Document>();
        listComics.forEach( comic -> {
            Document docComic = new Document("id", comic.getId())
                                    .append("title", comic.getTitle())
                                    .append("modified",comic.getModified())
                                    .append("format",comic.getFormat())
                                    .append("pageCount", comic.getPageCout())
                                    .append("description", comic.getDescription());
            comicsToInsert.add(docComic);
        });

        return MongoConnection.insertArrayIntoCharacter(comicsToInsert, id, "comics");
    }

    public static int insertCollaboratorsInCharacter(int id, HashMap<Integer, CollaboratorRawDTO> uniqueCollaborators){
        List<Document> collaboratorsToInsert = new ArrayList<Document>();
        for (Map.Entry<Integer,CollaboratorRawDTO> entry : uniqueCollaborators.entrySet() ){
            CollaboratorRawDTO collaborator = entry.getValue();
            Document mongoCollaborator = new Document("id", collaborator.getId())
                                            .append("firstName", collaborator.getFirstName())
                                            .append("lastName",collaborator.getLastName())
                                            .append("middleName",collaborator.getMiddleName())
                                            .append("fullName", collaborator.getFullName())
                                            .append("modified",collaborator.getModified());
            collaboratorsToInsert.add(mongoCollaborator);
        }
        return MongoConnection.insertArrayIntoCharacter(collaboratorsToInsert, id, "collaborators");
    }
}
