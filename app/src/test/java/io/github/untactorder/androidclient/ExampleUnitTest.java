package io.github.untactorder.androidclient;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import io.github.untactorder.network.NetworkService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends NetworkService {

    @Test
    public void addition_isCorrect() {

        Map<String,String> map = new HashMap<String,String>();
        map.put("id","tableName");
        map.put("pw","password");
        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println(json);

        System.out.println(makeJson("run","signin",json));

        //assertEquals(4, 2 + 2);
        /*
        String password = "123456";
        connect("127.0.0.1",50000);
        boolean loginCheck = signin("1");
        if(loginCheck) {
            String menu = send_recvMenu(makeJsonObject("get","/data/menu"));
            if(menu != null) {
                //...
                //고른메뉴 보내기
                String date = send_recvSelectMenu(makeJsonObject("put","new_order","{\"메인\": {\"0\": \"2\", \"1\": \"1\", \"2\": \"1\"},\n" +
                        " *                                                                  \"사이드\": {\"0\": \"2\", \"1\": \"1\", \"2\": \"1\"}\n" +
                        " *                                                                 }"));
                System.out.println(date);
            }

        }else{
            setPassword(makeJsonObject("run", "sign_up|sign_in","{\"id\": TABLE_NAME, \"pw\": PASSWORD}"));
        }

         */
    }
}