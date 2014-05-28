package helper;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class JSONHelper {

    public static <T> T convertToObject(JsonNode jsonNode, Class<T> clazz) {
        return Json.fromJson(jsonNode, clazz);
    }
}
