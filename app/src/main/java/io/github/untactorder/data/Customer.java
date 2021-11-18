package io.github.untactorder.data;

/**
 * 고객
 * @author 유채민
 */
public class Customer {
    protected final OrderList<Order> orderList = new OrderList();
    protected final String tableName;
    protected final String tablePassword;

    public Customer(String tableName, String tablePassword) {
        this.tableName = tableName;
        this.tablePassword = tablePassword;
    }
}
