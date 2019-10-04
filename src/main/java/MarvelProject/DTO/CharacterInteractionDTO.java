package MarvelProject.DTO;

import java.util.ArrayList;

public class CharacterInteractionDTO {
    private String character;
    private ArrayList<String> comics;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public ArrayList<String> getComics() {
        return comics;
    }

    public void setComics(ArrayList<String> comics) {
        this.comics = comics;
    }

    public CharacterInteractionDTO(String character, ArrayList<String> comics) {
        this.character = character;
        this.comics = comics;
    }
}
