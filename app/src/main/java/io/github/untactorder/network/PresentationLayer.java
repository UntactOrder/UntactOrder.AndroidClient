package io.github.untactorder.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <표현계층>
 * JSON 만드는 매서드!
 * 보안은 나중에..
 * @author 유채민
 * S -> J
 */
public interface PresentationLayer extends SessionLayer {

    default String makeJson(String method, String uri, Object value) {
        Map<String, Object> map = new HashMap<>();
        Gson gson = new Gson();
        if (method != null) {
            map.put("method", method);
        } else {
            throw new NullPointerException();
        }
        if (uri != null) {
            map.put("uri", uri);
        } else {
            throw new NullPointerException();
        }
        if (value != null) {
            map.put("value", value);
        }

        return gson.toJson(map);
    }

    default String makeJson(String method, String uri) {
        return makeJson(method, uri, null);
    }

    default Map<String, Object> makeMap(String jsn) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsn);

        Map<String, Object> map = new HashMap<>(), reqMap = new HashMap<>(), respMap = new HashMap<>();
        map.put("requested", reqMap);

        JsonElement request = element.getAsJsonObject().get("requested");
        JsonElement respond = element.getAsJsonObject().get("respond");

        String method = request.getAsJsonObject().get("method").getAsString();
        reqMap.put("method", method);
        String uri = request.getAsJsonObject().get("uri").getAsString();
        reqMap.put("uri", uri);
        if (!method.equals("get")) {
            String value = request.getAsJsonObject().get("value").toString();
            reqMap.put("requested", value);
        }
        /////
        String resp = respond.getAsString();
        map.put("respond", resp);
        return map;
    }

    default void get(String uri) throws IOException {
        send(makeJson("get", uri));
    }

    default void put(String uri, Object value) throws IOException {
        send(makeJson("put", uri, value));
    }

    default void run(String uri, Object value) throws IOException {
        send(makeJson("run", uri, value));
    }

    default Map<String, Object> get_respond() throws IOException {
        return makeMap( recv());
    }

}
