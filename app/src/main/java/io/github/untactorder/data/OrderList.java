package io.github.untactorder.data;

import java.util.ArrayList;

/**
 * 주문내역
 * @author 유채민
 */
public class OrderList<T extends Order> extends ArrayList<Order> {
    ArrayList<Order> orderList;
    /*
    OrderList(Order order) {
        orderList.add(order);
    }
     */
    public void addOrderList(Order order) {
        orderList.add(order);
    }
}
