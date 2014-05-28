package model.json.input.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.json.input.place.AddressPlace;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {

    @JsonProperty("data")
    private List<AddressPlace> addresses;

    public List<AddressPlace> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressPlace> addresses) {
        this.addresses = addresses;
    }
}
