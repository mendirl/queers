package model.json.input.geocode;

import model.json.input.place.AddressPlace;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
