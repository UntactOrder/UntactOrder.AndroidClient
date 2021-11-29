package io.github.untactorder.androidclient;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;

import io.github.untactorder.network.NetworkService;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientUnitTest extends NetworkService {
    @Test
    public void test1_addition_isCorrect() {
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
    public void test2_signIn_isWorking() throws IOException {
        if (tableCheck_isWorking().equals("none")) {
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
    public void test3_getMenuList_isWorking() throws IOException {
        Map<String, Object> menus = getMenuList();
        menus.forEach((id, val) -> {
            System.out.println(id+" : "+val);
        });
    }

    @Test
    public void test4_putNewOrder_isWorking() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("0", "2");
        map.put("1", "1");
        map.put("2", "1");
        String date = putNewOrder(map);
        System.out.println(date);
    }

    @Test
    public void test5_getOrderList_isWorking() throws IOException {
        Map<String, Object> orders = getOrderList(tableName);
        orders.forEach((id, val) -> {
            System.out.println(id+" : "+val);
        });
    }

    @Test
    public void test6_socketClose() throws IOException {
        close();
    }
}