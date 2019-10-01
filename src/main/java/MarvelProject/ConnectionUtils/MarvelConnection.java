package MarvelProject.ConnectionUtils;

import MarvelProject.Models.JsonResponseModel;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MarvelConnection {
    private static final String URL_MARVEL = "http://gateway.marvel.com";
    private static final String API_KEY = System.getenv("MARVEL_APP_KEY");
    private static final String HASH_KEY = System.getenv("MARVEL_PRI_KEY");
    private static final Logger logger = LogManager.getLogger("MarvelConnection");

    public static JsonResponseModel generateMarvelRequest(String urlEndpoint, List<NameValuePair> params) {

        CloseableHttpClient client = HttpClients.createDefault();
        String partialUrl = URL_MARVEL.concat(urlEndpoint);
        Date date= new Date();
        HttpGet getRequest = new HttpGet(partialUrl);
        getRequest.setHeader("Accept", "*/*");
        String timeStamp = String.valueOf(date.getTime());
        String hashedParams = DigestUtils.md5Hex(timeStamp+ HASH_KEY + API_KEY);

        params.add(new BasicNameValuePair("apikey",API_KEY));
        params.add(new BasicNameValuePair("ts", timeStamp));
        params.add(new BasicNameValuePair("hash", hashedParams));
        URI uri = null;
        HttpResponse response = null;

        try {
            uri = new URIBuilder(getRequest.getURI()).addParameters(params).build();
        }catch (URISyntaxException e) {
            logger.error(e);
        }

        getRequest.setURI(uri);

        try {
            response = client.execute(getRequest);
        } catch (IOException e) {
            logger.error(e);
        }
        String line = "";
        StringBuffer result = new StringBuffer();
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson g = new Gson();
        return g.fromJson(result.toString() , JsonResponseModel.class);

    }
}
