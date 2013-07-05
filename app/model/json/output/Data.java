package model.json.output;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Data {
    private DateTime date;
    private Set<Place> places;
    private Set<Place> velibs;
    private Set<Place> others;

    public Data() {
        places = new HashSet<Place>();
        velibs = new HashSet<Place>();
        others = new HashSet<Place>();
    }

    public Data(Set<Place> places, Set<Place> velibs, Set<Place> others) {
        this.places = places;
        this.velibs = velibs;
        this.others = others;
    }

    public void addDate(DateTime date) {
        this.date = date;
    }

    public DateTime getDate() {
        return date;
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
