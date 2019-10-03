/*
* Author: Arturo Velazquez Rios
* Starting point for the application. Place where we can defined routes and
* configuration for the API.
*/
package MarvelProject;

import MarvelProject.Controllers.CharacterController;
import MarvelProject.DAO.CharactersDAO;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class App {
    private static final Logger logger = LogManager.getLogger("HelloWorld");

    public static void main(String[] args) {
        port(8000);
        get("/marvel/collaborators/:character", CharacterController::getCharacterCollaborators);
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });
    }
}
