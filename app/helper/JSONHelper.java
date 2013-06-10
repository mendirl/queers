package helper;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import play.Logger;

import java.io.IOException;

public class JSONHelper {

    public static <T> T convertToObject(JsonNode jsonNode, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode, clazz);

        } catch (JsonMappingException e) {
            Logger.error("error json mapping => " + e);
        } catch (JsonParseException e) {
            Logger.error("error json parsing => " + e);
        } catch (IOException e) {
            Logger.error("error I/O => " + e);
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            Logger.error("error instantiation => " + e);
        } catch (IllegalAccessException e) {
            Logger.error("error illegal access => " + e);
        }

        return null;
    }
}
