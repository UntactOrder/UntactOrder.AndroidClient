package io.github.untactorder.data;

public class Customer {
    protected final OrderList<Order> orderList = new OrderList();
    protected final String tableName;
    protected final String tablePassword;

    Customer(String tableName, String tablePassword) {
        this.tableName = tableName;
        this.tablePassword = tablePassword;
    }
}
