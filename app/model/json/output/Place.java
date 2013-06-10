package model.json.output;

public class Place {

    private String name;
    private double lat;
    private double lng;
    private int all;
    private int left;
    private boolean valid;
    private double distance;

    public Place(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public void addVelib(int all, int left) {
        this.all += all;
        this.left += left;
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
