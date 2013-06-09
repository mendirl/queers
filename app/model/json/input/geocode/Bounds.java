package model.json.input.geocode;

import model.json.input.Coord;

public class Bounds {
    private Coord northeast;
    private Coord southwest;

    public Coord getNortheast() {
        return northeast;
    }

    public void setNortheast(Coord northeast) {
        this.northeast = northeast;
    }

    public Coord getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Coord southwest) {
        this.southwest = southwest;
    }
}
