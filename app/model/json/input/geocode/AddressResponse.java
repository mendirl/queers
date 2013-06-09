package model.json.input.geocode;

import model.json.input.park.AddressPark;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {

    @JsonProperty("data")
    private List<AddressPark> addresses;

    public List<AddressPark> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressPark> addresses) {
        this.addresses = addresses;
    }
}
