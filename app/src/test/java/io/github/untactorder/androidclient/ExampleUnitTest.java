package io.github.untactorder.androidclient;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import io.github.untactorder.network.NetworkService;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends NetworkService {
    @Test
    public void addition_isCorrect() {
        //assertEquals(4, 2 + 2);
        String password = "123456";
        connectToServer("",50000);
        boolean loginCheck = login(makeJsonObject("get",tableName));
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
    }
}