package model.json.output;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Data {
    private Set<Place> places;
    private Set<Place> velibs;
    private Set<Place> others;

    public Data() {
        places = new HashSet<>();
        velibs = new HashSet<>();
        others = new HashSet<>();
    }

    public Data(Set<Place> places, Set<Place> velibs, Set<Place> others) {
        this.places = places;
        this.velibs = velibs;
        this.others = others;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public Set<Place> getVelibs() {
        return velibs;
    }

    public Set<Place> getOthers() {
        return others;
    }
}
