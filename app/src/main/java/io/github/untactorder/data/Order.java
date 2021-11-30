package io.github.untactorder.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 주문
 * @author 유채민
 */
public class Order extends HashMap<String, String> {
    protected String orderId;
    protected int totalPrice;
    protected String orderMenuList;

    public Order(String orderId, Map order) {
        super((Map<String, String>) order);
        this.orderId = orderId;
        this.totalPrice = 0;
        this.orderMenuList = "";
        this.forEach((id, val) -> {
            Menu menu = MenuGroupAdapter.findMenuById(id);
            this.totalPrice += menu.getPrice() * Integer.parseInt((val));
            this.orderMenuList += menu.getName() + "  x" + val;
        });
    }

    public String getOrderId() {
        return orderId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getOrderMenuList() {
        return orderMenuList;
    }
}
