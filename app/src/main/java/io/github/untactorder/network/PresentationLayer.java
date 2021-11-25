package io.github.untactorder.network;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

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

    default String makeJson(String method, String uri, String value) {
        Map<String, String> map = new HashMap<String, String>();
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
        if (value != null) map.put("value", value);

        String json = gson.toJson(map);
        return json;
    }

    default String makeJson(String method, String uri) {
        return makeJson(method, uri, null);
    }

    default void get(String uri) throws IOException {
        send(makeJson("get", uri));
    }

    default void put(String uri, String value) throws IOException {
        send(makeJson("put", uri, value));
    }

    default void run(String uri, String value) throws IOException {
        send(makeJson("run", uri, value));
    }

}
