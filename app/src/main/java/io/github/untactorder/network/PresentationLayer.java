package io.github.untactorder.network;

import io.github.untactorder.data.Customer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * <표현계층>
 * JSON 만드는 매서드!
 * 보안은 나중에..
 * @author 유채민
 */
public interface PresentationLayer extends SessionLayer {

     default JSONObject makeJsonObject(String method, String uri, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("uri", uri);
            jsonObject.put("value", value);
        } catch (JSONException e) {}
        return jsonObject;
    }

    default JSONObject makeJsonObject(String method, String uri) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("uri", uri);
        } catch (JSONException e) {}
        return jsonObject;
    }

}
