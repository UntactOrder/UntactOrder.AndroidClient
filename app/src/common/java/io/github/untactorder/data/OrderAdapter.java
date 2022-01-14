package io.github.untactorder.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    protected static final String TAG = "OrderAdapter";

    protected static final ArrayList<Order> orderList = new ArrayList<>();
    protected static int totalOrderPrice = 0;
    protected static TextView totalPriceTextView = null;  // 메모리 누수
    protected static String krw = null;

    protected static GridLayoutManager layoutManager = null;
    protected static Integer gridSpan = null;
    protected static OrderAdapter self = null;

    public OrderAdapter(TextView total, GridLayoutManager manager, int gridSpan, String krw) {
        totalPriceTextView = total;
        layoutManager = manager;
        OrderAdapter.gridSpan = gridSpan;
        Log.d(TAG, "gridSpan = "+gridSpan);
        OrderAdapter.krw = krw;
        self = this;
        notifyChanged();
    }

    public static void notifyChanged() {
        if (self == null) return;

        if (totalPriceTextView != null) totalPriceTextView.setText((""+totalOrderPrice).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")+krw);

        if (self.getItemCount() == 1) {
            Log.d(TAG, "setSpanCount(1)");
            layoutManager.setSpanCount(1);
        } else if (layoutManager.getSpanCount() != gridSpan) {
            Log.d(TAG, "setSpanCount("+gridSpan+")");
            layoutManager.setSpanCount(gridSpan);
        }
        //https://todaycode.tistory.com/55
        self.notifyDataSetChanged();  // 사용하지 말자
    }

    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_ordr_list, parent, false);
        itemView.setSelected(true);
        itemView.setEnabled(false);
        return new OrderViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderViewHolder holder, int position) {
        holder.setItem(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static int size() {
        return orderList.size();
    }

    public static void setOrderListFromMap(Map<String, Map<String, Object>> orders) {
        clearItems();
        try {
            orders.forEach(OrderAdapter::addItemFromMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItemFromMap(String orderId, Map order) {
        addItem(new Order(orderId, order));
    }

    public static void addItem(@NonNull Order order) {
        orderList.add(order);
        totalOrderPrice += order.getTotalPrice();
        notifyChanged();
    }

    public static void removeItem(int index) {
        orderList.remove(index);
        notifyChanged();
    }

    public static void clearItems() {
        orderList.clear();
        notifyChanged();
    }

    public static void insertItem(@NonNull Order order, int position) {
        orderList.set(position, order);
        notifyChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        protected TextView orderId;
        protected TextView menu;
        protected TextView totalPrice;
        Context context;

        public OrderViewHolder(View itemView, Context context) {
            super(itemView);

            orderId = itemView.findViewById(R.id.ordr_list_tv_order_id);
            menu = itemView.findViewById(R.id.ordr_list_tv_menu);
            totalPrice = itemView.findViewById(R.id.ordr_list_tv_total_price);
            this.context = context;
        }

        public void setItem(Order order) {
            orderId.setText(order.getOrderTime());
            menu.setText(order.getOrderMenuList());
            totalPrice.setText("Total: "
                    + (""+order.getTotalPrice()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")
                    + context.getResources().getString(R.string.krw));
        }
    }
}
