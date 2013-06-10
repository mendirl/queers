package service;

import helper.JSONHelper;
import helper.WSHelper;
import model.json.input.Coord;
import model.json.input.geocode.AddressResponse;
import model.json.input.place.Address;
import model.json.input.place.AddressPlace;
import model.json.input.place.PlaceResponse;
import model.json.input.velib.VelibResponse;
import model.json.output.Place;
import play.libs.WS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapService {

    private static List<Place> parks;
    private static List<Place> museums;
    private static List<Place> velibs;

    public static List<Place> getVelibs() {
        return velibs;
    }

    public static List<Place> getParks() {
        return parks;
    }

    public static List<Place> getMuseums() {
        return museums;
    }

    public static void retrieveMap() {
        parks = new ArrayList<Place>();
        PlaceResponse parkResponse = retrievePark();
        transformPlace(parkResponse.getAddresses(), parks);

        museums = new ArrayList<Place>();
        PlaceResponse museumResponse = retrieveMuseum();
        transformPlace(museumResponse.getAddresses(), museums);

        velibs = new ArrayList<Place>();
        List<VelibResponse> velibResponses = retrieveVelib();
        transformVelib(velibResponses, velibs);

    }

    private static PlaceResponse retrievePark() {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipements/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String cid = "7";
        String offset = "10";
        String limit = "100";

        WS.Response wsResponse = WSHelper.ask(url, "token", token, "cid", cid, "offset", offset, "limit", limit);

        PlaceResponse parkResponse = JSONHelper.convertToObject(wsResponse.asJson(), PlaceResponse.class);

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

    private static PlaceResponse retrieveMuseum() {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipements/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String cid = "68";
        String offset = "10";
        String limit = "100";

        WS.Response wsResponse = WSHelper.ask(url, "token", token, "cid", cid, "offset", offset, "limit", limit);

        PlaceResponse museumResponse = JSONHelper.convertToObject(wsResponse.asJson(), PlaceResponse.class);

        return museumResponse;
    }

    public static void associate(List<Place> parks, List<Place> velibs, double radius) {
        for (Place park : parks) {
            for (Place velib : velibs) {
                Coord coordPark = new Coord(park.getLat(), park.getLng());
                double distance = calculate(coordPark, new Coord(velib.getLat(), velib.getLng()));
                if (distance < radius) {
                    velib.setDistance(distance);
                    velib.setValid(true);
                    park.addVelib(velib.getAll(), velib.getLeft());
                }
            }
        }
    }

    private static double calculate(Coord coordPark, Coord coordVelib) {
        double distance;

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

    private static void transformPlace(List<Address> addresses, List<Place> parks) {
        for (Address parkAddress : addresses) {
            AddressResponse addressResponse = retrieveCoord(parkAddress);
            AddressPlace addressPark = addressResponse.getAddresses().get(0);
            Place park = new Place(parkAddress.getName(), addressPark.getLat(), addressPark.getLon());
            parks.add(park);
        }
    }

    private static void transformVelib(List<VelibResponse> velibResponses, List<Place> velibs) {
        for (VelibResponse velibResponse : velibResponses) {
            Place velib = new Place(velibResponse.getName(), velibResponse.getPosition().getLat(), velibResponse.getPosition().getLng());
            velib.addVelib(velibResponse.getAvailableBikes(), velibResponse.getBikeStands());
            velibs.add(velib);
        }
    }


}
