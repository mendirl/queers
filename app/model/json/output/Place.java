package model.json.output;

public class Place {

    private int id;
    private String name;
    private double lat;
    private double lng;
    private int all;
    private int left;
    private double distance;

    public Place(int id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public void addVelib(int all, int left) {
        this.all += all;
        this.left += left;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getAll() {
        return all;
    }

    public int getLeft() {
        return left;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
