package service;

import helper.JSONHelper;
import helper.WSHelper;
import model.json.input.Coord;
import model.json.input.geocode.AddressResponse;
import model.json.input.park.Address;
import model.json.input.park.AddressPark;
import model.json.input.park.ParkResponse;
import model.json.input.velib.VelibResponse;
import model.json.output.Park;
import play.libs.WS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapService {

    private static List<Park> parks;

    public static List<VelibResponse> getVelibs() {
        return velibs;
    }

    public static List<Park> getParks() {
        return parks;
    }

    private static List<VelibResponse> velibs;

    public static void retrieveMap() {
        parks = new ArrayList<Park>();

        ParkResponse parkResponse = retrievePark();
        velibs = retrieveVelib();

        transform(parkResponse.getAddresses(), parks);

        associate(parks, velibs, 0.4);
    }

    private static ParkResponse retrievePark() {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipements/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String cid = "7";
        String offset = "10";
        String limit = "100";

        WS.Response wsResponse = WSHelper.ask(url, "token", token, "cid", cid, "offset", offset, "limit", limit);

        ParkResponse parkResponse = JSONHelper.convertToObject(wsResponse.asJson(), ParkResponse.class);

        return parkResponse;
    }

    private static List<VelibResponse> retrieveVelib() {
        String url = "https://api.jcdecaux.com/vls/v1/stations";
        String apiKey = "a56c9c8e11df2ff056888f0add2da816c3a41c91";
        String contract = "Paris";

        WS.Response wsResponse = WSHelper.ask(url, "apiKey", apiKey, "contract", contract);

        VelibResponse[] velibResponse = JSONHelper.convertToObject(wsResponse.asJson(), VelibResponse[].class);

        return Arrays.asList(velibResponse);
    }

    private static void associate(List<Park> parks, List<VelibResponse> velibs, double radius) {
        for (Park park : parks) {
            for (VelibResponse velibResponse : velibs) {
                Coord coordPark = new Coord(park.getLat(), park.getLng());
                double distance = calculate(coordPark, velibResponse.getPosition());
                if (distance < radius) {
                    velibResponse.setDistance(distance);
                    velibResponse.setValid(true);
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

    private static AddressResponse retrieveCoord(Address parkAddress) {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipement/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String id = String.valueOf(parkAddress.getId());

        WS.Response wsResponse = WSHelper.ask(url, "token", token, "id", id);

        AddressResponse addressResponse = JSONHelper.convertToObject(wsResponse.asJson(), AddressResponse.class);

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
