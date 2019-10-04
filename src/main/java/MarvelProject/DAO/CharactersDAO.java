package MarvelProject.DAO;

import MarvelProject.ConnectionUtils.MongoConnection;
import MarvelProject.DTO.CharacterRawDTO;
import MarvelProject.DTO.CollaboratorRawDTO;
import MarvelProject.DTO.ComicRawDTO;

import java.util.*;

import org.bson.Document;

public class CharactersDAO {


    public static Integer insertCharacters(List<CharacterRawDTO> listCharacters, Date date){
        List<Document> documentsToInsert = new ArrayList<Document>();
        listCharacters.forEach(character -> {
            Document docCharacter = new Document("name", character.getName())
                                        .append("_id", character.getId())
                                        .append("description", character.getDescription())
                                        .append("modified", character.getModified())
                                        .append("last_sync", date.toString());

            documentsToInsert.add(docCharacter);
        });
        return MongoConnection.insertManyCharacters(documentsToInsert);
    }

    public static Integer insertComicsInCharacter(int id, List<ComicRawDTO> listComics){
        List<Document> comicsDocuments = generateComicDocuments(listComics);

        return MongoConnection.insertArrayIntoCharacter(comicsDocuments, id, "comics");
    }


    public static Integer updateComicsInCharacter(int id,List<ComicRawDTO> listComics ){
        List<Document> comicsDocuments = generateComicDocuments(listComics);
        return MongoConnection.updateArrayComicsIntoCharacter(comicsDocuments,id);
    }

    public static Integer insertCollaboratorsInCharacter(int id, HashMap<Integer, CollaboratorRawDTO> uniqueCollaborators){
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

    private static List<Document> generateComicDocuments(List<ComicRawDTO> listComics){
        List<Document> comicsToInsert = new ArrayList<Document>();
        listComics.forEach( comic -> {
            Document docComic = new Document("id", comic.getId())
                    .append("title", comic.getTitle())
                    .append("modified",comic.getModified())
                    .append("format",comic.getFormat())
                    .append("pageCount", comic.getPageCount())
                    .append("description", comic.getDescription());
            comicsToInsert.add(docComic);
        });
        return comicsToInsert;
    }

}
