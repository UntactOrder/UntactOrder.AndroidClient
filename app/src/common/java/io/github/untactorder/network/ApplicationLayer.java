package io.github.untactorder.network;

import android.util.Log;
import io.github.untactorder.data.Order;
import io.github.untactorder.data.OrderAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    String TAG = "ApplicationLayer";

    default String tableCheck(int tableName, String ip, int port) throws IOException {
        Log.d(TAG, "Try to Connect");
        connect(ip, port);

        Log.d(TAG, "Try to TableCheck");
        get("/customer/"+tableName);
        return (String) getRespond().get("respond");
    }

    default String signUp(int id, String pw) throws IOException {
        Log.d(TAG, "signUp Method");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id); map.put("pw", pw);
        Log.d(TAG, "id:"+id+"/pw:"+pw);
        run("sign_up", map);
        return (String) getRespond().get("respond");
    }

    default String signIn(int id, String pw) throws IOException {
        Log.d(TAG, "signIn Method");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id); map.put("pw", pw);
        Log.d(TAG, "id:"+id+"/pw:"+pw);
        run("sign_in", map);
        return (String) getRespond().get("respond");
    }

    default Map<String, Map<String, Object>> getMenuList() throws IOException {
        get("/data/menu");
        return (Map<String, Map<String, Object>>) getRespond().get("respond");
    }

    default String putNewOrder(Map<String, String> order) throws IOException {
        put("new_order",order);

        Map<String, Object> map = getRespond();
        if (Objects.equals((String) map.get("respond"), "success")) {
            Map<String, Object> req = (Map<String, Object>) map.get("requested");
            return (String) req.get("value");
        } else {
            return null;
        }
    }

    default Map<String, Map<String, Object>> getOrderList(int tableName) throws IOException {
        get("/customer/"+tableName+"/orderlist");
        return (Map<String, Map<String, Object>>) getRespond().get("respond");
    }
}
