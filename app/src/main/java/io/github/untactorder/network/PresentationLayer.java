package io.github.untactorder.network;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <표현계층>
 * JSON 만드는 매서드!
 * Gson gson = new Gson();
 *         map.put("id","1");
 *         map.put("pw","123456");
 *         String json = gson.toJson(map);
 *         String sign = sign_up(json);
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

    default Map<String, Object> getMapFromJson(String jsn) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(jsn, map.getClass());
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

    default Map<String, Object> getRespond() throws IOException {
        return getMapFromJson( recv());
    }

    default Map<String, Object> getRequest() throws IOException {
        return getMapFromJson( recv());
    }
}
