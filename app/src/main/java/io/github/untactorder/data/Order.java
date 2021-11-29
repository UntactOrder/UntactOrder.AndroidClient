package io.github.untactorder.data;

/**
 * 주문
 * @author 유채민
 */
public class Order {
    protected String orderId;
    protected String menu;
    protected int totalPrice;

    public Order(String orderId, String menu, int totalPrice) {
        this.orderId = orderId;
        this.menu = menu;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMenu() {
        return menu;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
