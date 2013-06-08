package controllers;

import model.ParkResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.map;

import java.io.IOException;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result map() {

        WS.WSRequestHolder holder = WS.url("https://api.paris.fr:3000/data/1.0/Equipements/get_equipements/?token=2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730&cid=7&offset=10&limit=100");
        F.Promise<WS.Response> promise = holder.get();
        WS.Response response = promise.get();

        JsonNode node = response.asJson();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ParkResponse parkResponse = mapper.readValue(node, ParkResponse.class);


        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return ok(map.render("Les Petites Pédales", "AIzaSyA7PTIhHAxOHkXscvX2JKQ849rsqW1VhpY"));
    }

}