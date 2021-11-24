package io.github.untactorder.network;

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
 *                                        "1": {"name": "새우 베이컨 필라프", "price": "13500", "pinned":"true"}
 *                                       }
 *                              }"
 *                  }
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

    String tableName = "1"; //임시 tableName
    default boolean login(JSONObject jsonObject) {
        //로그인요청
        sendToServer(jsonObject);
        try {
            String data = recv();
            try{
                JSONObject json = new JSONObject(data);
                String respond = (String)json.get("respond");
                if(respond.equals("ok")) {
                    return true;
                }else if(respond.equals("none")) {
                    return false;
                }
            } catch (JSONException e) {}
        } catch (IOException e) {}
        return false;
    }

    default boolean setPassword(JSONObject jsonObject) {
        sendToServer(jsonObject);
        try {
            String data = recv();
            try{
                JSONObject json = new JSONObject(data);
                String respond = (String)json.get("respond");
                if(respond.equals("ok")) {
                    return true;
                }else if(respond.equals("wrong_pw")) {
                    return false;
                }
            } catch (JSONException e) {}
        } catch (IOException e) {}
        return false;
    }

    default String send_recvMenu(JSONObject jsonObject) {
        sendToServer(jsonObject);
        try {
            String data = recv();
            try{
                JSONObject json = new JSONObject(data);
                String respond = (String)json.get("respond");
                return respond;
            } catch (JSONException e) {}
        } catch (IOException e) {}
        return null;
    }

    default String send_recvSelectMenu(JSONObject jsonObject) {
        sendToServer(jsonObject);
        try {
            String data = recv();
            try{
                JSONObject json = new JSONObject(data);
                String respond = (String)json.get("requested");
                JSONObject json2 = new JSONObject(respond);
                String date = (String)json2.get("value");
                return date;
            } catch (JSONException e) {}
        } catch (IOException e) {}
        return null;
    }

    default void sendToServer(JSONObject jsonObject) {
        String data = jsonObject.toString();
        try {
            send(data);
            System.out.println("Complete Send!");
        } catch (IOException e) {System.out.println("ERROR");}
    }
    /*
    default JSONObject recvFromServer() {
        try {
            try {
                String data = recv();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requested",data);
                jsonObject.put("respond",data);
            } catch (IOException e) {}
        } catch (JSONException e) {}
    }

     */

}
