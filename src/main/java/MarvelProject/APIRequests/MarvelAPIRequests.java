package MarvelProject.APIRequests;

import MarvelProject.ConnectionUtils.MarvelConnection;
import MarvelProject.Models.JsonResponseModel;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MarvelAPIRequests {
    public static JsonResponseModel getCharactersRequest(String characterNane){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name",characterNane));
        return MarvelConnection
                .generateMarvelRequest("/v1/public/characters",params);
    }

    public static JsonResponseModel getComicsFromCharacterRequest(int characterId){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String baseUrl ="/v1/public/characters/" + characterId +"/comics";
        return MarvelConnection.generateMarvelRequest(baseUrl, params);
    }

    public static JsonResponseModel getCollaborators(int comicId){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String baseUrl = "/v1/public/comics/" + comicId + "/creators";
        return MarvelConnection.generateMarvelRequest(baseUrl, params);
    }
}
