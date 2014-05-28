package model.json.input.geocode;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    @JsonProperty("address_components")
    private List<Component> addressComponents;
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
    private List<String> types;

    public List<Component> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<Component> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
