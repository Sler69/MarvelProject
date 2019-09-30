/*
* Author: Arturo Velazquez Rios
* Starting point for the application. Place where we can defined routes and
* configuration for the API.
*/
package MarvelProject;

import spark.Request;
import spark.Response;

import java.util.logging.Logger;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        port(8000);
        System.out.println("LOL");
        get("/hello", App::hello);

    }

    public static String hello(Request req, Response res){
        return "LOL";
    }
}
