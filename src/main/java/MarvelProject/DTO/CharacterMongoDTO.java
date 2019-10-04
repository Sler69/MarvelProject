package MarvelProject.DTO;

import java.util.ArrayList;

public class CharacterMongoDTO {
    private int _id;
    private String name;
    private String description;
    private String modified;
    private ArrayList<ComicRawDTO> comics;
    private ArrayList<CollaboratorRawDTO> collaborators;

    public String getLast_sync() {
        return last_sync;
    }

    public void setLast_sync(String last_sync) {
        this.last_sync = last_sync;
    }

    private String last_sync;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public ArrayList<ComicRawDTO> getComics() {
        return comics;
    }

    public void setComics(ArrayList<ComicRawDTO> comics) {
        this.comics = comics;
    }

    public ArrayList<CollaboratorRawDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<CollaboratorRawDTO> collaborators) {
        this.collaborators = collaborators;
    }
}
