package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.json.output.Data;
import play.libs.Json;
import play.libs.Jsonp;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.MapService;
import views.html.gmap;
import views.html.index;
import views.html.mapq;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Les Petites Pédales"));
    }

    public static Result gmap(double rad, double lat, double lng, String place, boolean all) {
        Data data = calculate(rad, lat, lng, place, all);
        return ok(gmap.render("Les Petites Pédales", "AIzaSyA7PTIhHAxOHkXscvX2JKQ849rsqW1VhpY", data));
    }

    public static Result mapq(double rad, double lat, double lng, String place, boolean all) {
        Data data = calculate(rad, lat, lng, place, all);
        return ok(mapq.render("Les Petites Pédales", "Fmjtd%7Cluub2gu7nu%2Cr0%3Do5-9ua01f", data));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result api(double rad, double lat, double lng, String place, boolean all, String callback) {
        Data data = calculate(rad, lat, lng, place, all);
        JsonNode node = Json.toJson(data);
        // return jsonp for cross-domain call
        if (callback != null) {
            return ok(Jsonp.jsonp(callback, node));
        }

        return ok(node);
    }

    private static Data calculate(double rad, double lat, double lng, String place, boolean all) {
        return MapService.associate(rad, lat, lng, place, all);
    }


}