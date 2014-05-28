package model.json.input.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.json.input.Coord;


public class Geometry {

    public enum LocationType {APPROXIMATE, RANGE_INTERPOLATED, ROOFTOP, GEOMETRIC_CENTER}

    private Bounds bounds;
    private Coord location;
    @JsonProperty("location_type")
    private LocationType locationType;
    private Bounds viewport;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Coord getLocation() {
        return location;
    }

    public void setLocation(Coord location) {
        this.location = location;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Bounds getViewport() {
        return viewport;
    }

    public void setViewport(Bounds viewport) {
        this.viewport = viewport;
    }
}
