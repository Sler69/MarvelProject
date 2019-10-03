/*
* Author: Arturo Velazquez Rios
* Starting point for the application. Place where we can defined routes and
* configuration for the API.
*/
package MarvelProject;

import MarvelProject.Controllers.CharacterController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static spark.Spark.*;

public class App {
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        port(8000);
        get("/marvel/collaborators/:character", CharacterController::getCharacterCollaborators);
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });
    }
}
