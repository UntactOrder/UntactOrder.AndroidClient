package io.github.untactorder.androidclient;

import org.junit.Test;
import static org.junit.Assert.*;

import io.github.untactorder.network.NetworkService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ClientUnitTest extends NetworkService {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    int tableName = 1;
    String pw = "123456";
    String ip = "127.0.0.1";
    Integer port = 53321;

    public String tableCheck_isWorking() throws IOException {
        String status = tableCheck(tableName, ip,port);
        System.out.println(status);
        return status;
    }

    @Test
    public void signIn_isWorking() throws IOException {
        String status = tableCheck_isWorking();
        if (status.equals("none")) {
            signUp_isWorking();
        } else {
            String result = signIn(tableName, pw);
            System.out.println(result);
        }
    }

    public void signUp_isWorking() throws IOException {
        String result = signUp(1, pw);
        System.out.println(result);
    }

    @Test
    public void getMenuList_isWorking() throws IOException {
        String menus = getMenuList();
        System.out.println(menus);
    }

    @Test
    public void putNewOrder_isWorking() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("0", "2");
        map.put("1", "1");
        map.put("2", "1");
        String date = putNewOrder(map);
        System.out.println(date);
    }

    @Test
    public void getOrderList_isWorking() throws IOException {
        String orders = getOrderList(tableName);
        System.out.println(orders);
    }
}