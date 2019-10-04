/*
* Author: Arturo Velazquez Rios
* Starting point for the application. Place where we can defined routes and
* configuration for the API.
*/
package MarvelProject;

import MarvelProject.Controllers.CharacterController;
import MarvelProject.Controllers.CollaboratorController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class App {
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        port(8000);
        path("/marvel", () -> {
            before("/*", (q, a) -> logger.info("Received api call for marvel"));
            path("/collaborators", () -> {
                get("/:character", CollaboratorController::getCharacterCollaborators);
            });
            path("/characters", () -> {
                get("/:character", CharacterController::getCharacterInteraction);
            });
        });
    }
}
