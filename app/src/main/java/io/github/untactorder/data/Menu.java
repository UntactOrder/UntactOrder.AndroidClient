package io.github.untactorder.data;

public class Menu {
    protected String id;
    protected String name;
    protected int price;
    protected int quantity = 0;

    public Menu(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
       return name;
    }

    public int getPrice() {
        return price;
    }

    public synchronized int getQuantity() {
        return quantity;
    }

    public synchronized int increaseQuantity() {
        if (quantity != Integer.MAX_VALUE) quantity++;
        return quantity;
    }

    public synchronized int decreaseQuantity() {
        if (quantity != 0) quantity--;
        return quantity;
    }

    public synchronized void setQuantityToZero() {
        quantity = 0;
    }
}
