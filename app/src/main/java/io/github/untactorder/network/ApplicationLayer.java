package io.github.untactorder.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * <응용계층>
 * qr에서 테이블번호받기 -> 로그인요청 -> 테이블번호에대해 비밀번호 등록여부확인
 * send to server : {"method": "get", "uri": "/customer/TABLE_NAME"}
 * * recv : respond = {"requested": {"method": "get", "uri": "/customer/TABLE_NAME"},
 *                     "respond": "ok|none"}
 * }
 * 등록x -> 등록하기(비밀번호 새로생성,보내기)
 * (3번 반복)등록o -> 일치 ->
 * send to server : {"method": "run", "uri": "sign_up|sign_in", "value": {"id": TABLE_NAME, "pw": PASSWORD}
 *                  }
 * * recv : respond = {"requested": {"method": "run", "uri": "sign_up|sign_in", "value": TABLE_NAME},
 *                     "respond": "ok|wrong_pw"
 *                    }
 * 일치or등록 -> 메뉴 요청 및 받아오기(서버)
 * send to server : {"method": "get", "uri": "/data/menu"}
 * recv : respond = {"requested": {"method": "get","uri": "/data/menu"},
 *                   "respond": "{"메인": {"0": {"name": "봉골레 파스타", "price": "12000", "pinned":"true"},
 *                                      }
 *  *                              }"
 *  *                                             "1": {"name": "새우 베이컨 필라프", "price": "13500", "pinned":"true"}
 *              }
 * -> 고른메뉴 보내기(서버로)
 * send to server : {"method": "put", "uri": "new_order", "value": {"메인": {"0": "2", "1": "1", "2": "1"},
 *                                                                  "사이드": {"0": "2", "1": "1", "2": "1"}
 *                                                                 }
 *                  }
 * recv : respond = {"requested": {"method": "put", "uri": "new_order", "value": "2021.11.18 22:08"},
 *                   "respond": "success"
 *                  }
 * * @author 유채민
 */
public interface ApplicationLayer extends PresentationLayer {

    default String tableCheck(String tableName) {
        connect("127.0.0.1",51103);
        try {
            get("/customer/"+tableName);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            return respond;
        } catch (IOException e) {}
        return null;
    }

    default String sign_up(String value) {
        try {
            run("sign_up", value);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            if (respond.equals("ok")) {
                return "ok";
            } else{
                return "wrong password";
            }
        } catch (IOException e) {}
        return "Exception";
    }

    default String sign_in(String value) {
        try {
            run("sign_in", value);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            if (respond.equals("ok")) {
                return "ok";
            } else{
                return "wrong password";
            }
        } catch (IOException e) {}
        return "Exception";
    }
    /*
    default boolean sign_in(String tableName) {
        try {
            get(tableName);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            if (respond.equals("ok")) {
                return true;
            } else if (respond.equals("none")) {
                return false;
            } else {
                return false;
            }
        } catch (IOException e) { return false;}
    }

     */
    //uri = sign_up/sign_in value = {"id": TABLE_NAME, "pw": PASSWORD}
    default boolean setPassword(String uri, String value) {
        try {
            run(uri,value);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            if (respond.equals("ok")) {
                return true;
            } else if (respond.equals("wrong_pw")) {
                return false;
            } else {
                return false;
            }
        } catch (IOException e) {return false;}
    }

    default String send_recvMenu() {
        try {
            get("/data/menu");
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            return respond;
        } catch (IOException e) {return null;}
    }

    default String send_recvSelectMenu(String value) {
        try {
            put("new_order",value);
            String data = recv();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(data);
            String respond = element.getAsJsonObject().get("respond").getAsString();
            if (respond.equals("success")) {
                String request = element.getAsJsonObject().get("request").getAsString();
                JsonElement parse = parser.parse(request);
                String date = element.getAsJsonObject().get("value").getAsString();
                return date;
            } else {
                return null;
            }
        } catch (IOException e) {return null;}
    }
}
