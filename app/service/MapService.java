package service;

import helper.JSONHelper;
import helper.WSHelper;
import model.json.input.Coord;
import model.json.input.geocode.AddressResponse;
import model.json.input.place.Address;
import model.json.input.place.AddressPlace;
import model.json.input.place.PlaceResponse;
import model.json.input.velib.VelibResponse;
import model.json.output.Data;
import model.json.output.Place;
import play.libs.WS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapService {

    private static List<Place> staticParks;
    private static List<Place> staticMuseums;
    private static List<Place> staticVelibs;

    public static void retrieveMap() {
        staticParks = new ArrayList<Place>();
        PlaceResponse parkResponse = retrievePark();
        transformPlace(parkResponse.getAddresses(), staticParks);

        staticMuseums = new ArrayList<Place>();
        PlaceResponse museumResponse = retrieveMuseum();
        transformPlace(museumResponse.getAddresses(), staticMuseums);

        staticVelibs = new ArrayList<Place>();
        List<VelibResponse> velibResponses = retrieveVelib();
        transformVelib(velibResponses, staticVelibs);

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

    public static Data associate(double rad, double lat, double lng, String type, boolean all) {
        List<Place> places = new ArrayList<Place>();
        // specific place around me
        if (rad != 0 && lat != 0 && lng != 0 && type != null) {
            if ("park".equals(type)) {
                places.addAll(findPlace(staticParks, rad, lat, lng));
            } else if ("museum".equals(type)) {
                places.addAll(findPlace(staticMuseums, rad, lat, lng));
            }
            // wher i am
        } else if (rad != 0 && lat != 0 && lng != 0) {
            Place place = new Place("My place", lat, lng);

            places.add(place);

        } else if (type != null) {
            if ("park".equals(type)) {
                places.addAll(staticParks);
            } else if ("museum".equals(type)) {
                places.addAll(staticMuseums);
            }
        } else {
            Data data = new Data();
            data.getVelibs().addAll(staticVelibs);
            return data;
        }

        return associate(places, rad, all);
    }

    public static List<Place> findPlace(List<Place> places, double radius, double lat, double lng) {
        List<Place> result = new ArrayList<Place>();
        Coord myCoord = new Coord(lat, lng);

        for (Place place : places) {
            Coord coordPlace = new Coord(place.getLat(), place.getLng());
            double distance = calculate(myCoord, coordPlace);
            if (distance < radius) {
                result.add(place);
            }
        }

        return result;
    }

    public static Data associate(List<Place> places, double radius, boolean all) {
        Data data = new Data();
        for (Place place : places) {
            data.getPlaces().add(place);
            Coord coordPlace = new Coord(place.getLat(), place.getLng());
            for (Place velib : staticVelibs) {
                Coord coordVelib = new Coord(velib.getLat(), velib.getLng());
                double distance = calculate(coordPlace, coordVelib);
                if (distance < radius) {
                    velib.setDistance(distance);
                    place.addVelib(velib.getAll(), velib.getLeft());
                    data.getVelibs().add(velib);
                } else if (all) {
                    velib.setDistance(0);
                    data.getOthers().add(velib);
                }
            }
        }
        data.getOthers().removeAll(data.getVelibs());
        return data;
    }

    private static double calculate(Coord coordPlace, Coord coordVelib) {
        double distance;

        int R = 6371; // km
        double dLat = Math.toRadians(coordVelib.getLat() - coordPlace.getLat());
        double dLon = Math.toRadians(coordVelib.getLng() - coordPlace.getLng());
        double lat1 = Math.toRadians(coordVelib.getLat());
        double lat2 = Math.toRadians(coordPlace.getLat());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        distance = R * c;

        return distance;
    }

    private static AddressResponse retrieveCoord(Address placeAddress) {
        String url = "https://api.paris.fr:3000/data/1.0/Equipements/get_equipement/";
        String token = "2e9e21d281009d7c585dfd981ced7c52baf0c3bd9f7b23ebb41960d2c954df9e32404d83c7ab86d1e6ef77c0dfd94730";
        String id = String.valueOf(placeAddress.getId());

        WS.Response wsResponse = WSHelper.ask(url, "token", token, "id", id);

        AddressResponse addressResponse = JSONHelper.convertToObject(wsResponse.asJson(), AddressResponse.class);

        return addressResponse;
    }

    private static void transformPlace(List<Address> addresses, List<Place> places) {
        for (Address address : addresses) {
            AddressResponse addressResponse = retrieveCoord(address);
            AddressPlace addressPlace = addressResponse.getAddresses().get(0);
            Place place = new Place(address.getName(), addressPlace.getLat(), addressPlace.getLon());
            places.add(place);
        }
    }

    private static void transformVelib(List<VelibResponse> velibResponses, List<Place> velibs) {
        for (VelibResponse velibResponse : velibResponses) {
            Place velib = new Place(velibResponse.getName(), velibResponse.getPosition().getLat(), velibResponse.getPosition().getLng());
            velib.addVelib(velibResponse.getBikeStands(), velibResponse.getAvailableBikes());
            velibs.add(velib);
        }
    }


}
