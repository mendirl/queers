package controllers;

import model.json.input.Coord;
import model.json.input.geocode.AddressResponse;
import model.json.input.park.Address;
import model.json.input.park.AddressPark;
import model.json.input.park.ParkResponse;
import model.json.input.velib.VelibResponse;
import model.json.output.Park;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result map() {
        List<Park> parks = new ArrayList<Park>();

        ParkResponse parkResponse = retrievePark();
        List<VelibResponse> velibs = retrieveVelib();

        transform(parkResponse.getAddresses(), parks);

        associate(parks, velibs, 0.5);


        return ok(map.render("Les Petites Pédales", "AIzaSyA7PTIhHAxOHkXscvX2JKQ849rsqW1VhpY", parks, velibs));
    }

    private static void associate(List<Park> parks, List<VelibResponse> velibs, double radius) {
        for (Park park : parks) {
            for (VelibResponse velibResponse : velibs) {
                Coord coordPark = new Coord(park.getLat(), park.getLng());
                if (calculate(coordPark, velibResponse.getPosition()) < radius) {
                    park.addVelib(velibResponse.getBikeStands(), velibResponse.getAvailableBikes());
                }
            }
        }


    }

    private static double calculate(Coord coordPark, Coord coordVelib) {
        double distance = 0;

        int R = 6371; // km
        double dLat = Math.toRadians(coordVelib.getLat() - coordPark.getLat());
        double dLon = Math.toRadians(coordVelib.getLng() - coordPark.getLng());
        double lat1 = Math.toRadians(coordVelib.getLat());
        double lat2 = Math.toRadians(coordPark.getLat());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        distance = R * c;

        return distance;
    }

    /**
     * @param url
     * @param params
     * @return
     */
    private static WS.Response askWs(String url, String... params) {
        WS.WSRequestHolder holder = WS.url(url);

        for (int i = 0; i < params.length - 1; i = i + 2) {
            holder.setQueryParameter(params[i], params[i + 1]);
        }

        F.Promise<WS.Response> promise = holder.get();
        return promise.get();
    }

    /**
     * @param jsonNode
     * @param clazz
     * @param <T>
     */
    private static <T> T convertJSONToObject(JsonNode jsonNode, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode, clazz);

        } catch (JsonMappingException e) {
            Logger.error("error json mapping => " + e);
        } catch (JsonParseException e) {
            Logger.error("error json parsing => " + e);
        } catch (IOException e) {
            Logger.error("error I/O => " + e);
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            Logger.error("error instantiation => " + e);
        } catch (IllegalAccessException e) {
            Logger.error("error illegal access => " + e);
        }

        return null;
    }

    /**
     * @return
     */
    private static ParkResponse retrievePark() {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipements/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String cid = "7";
        String offset = "10";
        String limit = "100";

        WS.Response wsResponse = askWs(url, "token", token, "cid", cid, "offset", offset, "limit", limit);

        ParkResponse parkResponse = convertJSONToObject(wsResponse.asJson(), ParkResponse.class);

        return parkResponse;
    }

    /**
     * @return
     */
    private static List<VelibResponse> retrieveVelib() {
        String url = "https://api.jcdecaux.com/vls/v1/stations";
        String apiKey = "a56c9c8e11df2ff056888f0add2da816c3a41c91";
        String contract = "Paris";

        WS.Response wsResponse = askWs(url, "apiKey", apiKey, "contract", contract);

        VelibResponse[] velibResponse = convertJSONToObject(wsResponse.asJson(), VelibResponse[].class);

        return Arrays.asList(velibResponse);
    }

    /**
     * @param parkAddress
     * @return
     */
    private static AddressResponse retrieveCoord(Address parkAddress) {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipement/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String id = String.valueOf(parkAddress.getId());

        WS.Response wsResponse = askWs(url, "token", token, "id", id);

        AddressResponse addressResponse = convertJSONToObject(wsResponse.asJson(), AddressResponse.class);

        return addressResponse;
    }

    private static void transform(List<Address> addresses, List<Park> parks) {
        for (Address parkAddress : addresses) {
            AddressResponse addressResponse = retrieveCoord(parkAddress);
            AddressPark addressPark = addressResponse.getAddresses().get(0);
            Park park = new Park(parkAddress.getName(), addressPark.getLat(), addressPark.getLon());
            parks.add(park);
        }
    }
}