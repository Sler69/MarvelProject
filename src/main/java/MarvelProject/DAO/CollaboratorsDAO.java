package MarvelProject.DAO;

import MarvelProject.ConnectionUtils.MarvelConnection;
import MarvelProject.Models.JsonResponseModel;
import com.google.gson.JsonObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorsDAO {

    public static final JsonObject getCollaborators() {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name","Iron Man"));
        JsonResponseModel responseForCharacter = MarvelConnection
                .generateMarvelRequest("/v1/public/characters",params);
        return responseForCharacter.getData();
    }
}
