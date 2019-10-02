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
        JsonResponseModel responseForCharacter = MarvelConnection
                .generateMarvelRequest("/v1/public/characters",params);
        return responseForCharacter;
    }
}
