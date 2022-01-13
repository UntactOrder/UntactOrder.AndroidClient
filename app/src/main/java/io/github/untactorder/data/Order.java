package io.github.untactorder.data;

import android.icu.text.SimpleDateFormat;
import java.text.ParseException;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 주문
 * @author 유채민
 */
public class Order extends HashMap<String, String> {
    static String fromDatePattern = "yyyyMMddHHmmss";
    static String toDatePattern = "yyyy-MM-dd HH:mm:ss";
    static SimpleDateFormat fromDateFormat = new SimpleDateFormat(fromDatePattern);
    static SimpleDateFormat toDateFormat = new SimpleDateFormat(toDatePattern);

    protected String orderId;
    protected String orderTime;
    protected int totalPrice;
    protected String orderMenuList;

    public Order(String orderId, Map order) {
        super((Map<String, String>) order);
        this.orderId = orderId;
        String[] parse = orderId.split("[a-zA-Z]");
        this.orderTime = parse[parse.length - 1].substring(0, 14);
        try {
            Date date = fromDateFormat.parse(orderTime);
            orderTime = toDateFormat.format(date);
        } catch (ParseException e) {
            Log.d("Order", "dateStr : " + orderTime + ", datePattern:" + toDatePattern);
            e.printStackTrace();
        }
        this.totalPrice = 0;
        this.orderMenuList = "";
        this.forEach((id, val) -> {
            Menu menu = MenuGroupAdapter.findMenuById(id);
            this.totalPrice += menu.getPrice() * Integer.parseInt((val));
            this.orderMenuList += menu.getName() + "[" + menu.getPrice() + "]  x" + val + "\n";
        });
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getOrderMenuList() {
        return orderMenuList;
    }
}
