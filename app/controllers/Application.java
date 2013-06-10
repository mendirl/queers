package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import service.MapService;
import views.html.index;
import views.html.gmap;
import views.html.mapq;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Les Petites Pédales"));
    }

    public static Result gmap() {
        calculate();
        return ok(gmap.render("Les Petites Pédales", "AIzaSyA7PTIhHAxOHkXscvX2JKQ849rsqW1VhpY", MapService.getParks(), MapService.getVelibs()));
    }

    public static Result mapq() {
        calculate();
        return ok(mapq.render("Les Petites Pédales", "Fmjtd%7Cluub2gu7nu%2Cr0%3Do5-9ua01f", MapService.getParks(), MapService.getVelibs()));
    }

    private static void calculate(){
        MapService.associate(MapService.getParks(), MapService.getVelibs(), 0.4);
    }


}