package io.github.untactorder.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    protected static final ArrayList<Order> orderList = new ArrayList<>();

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

    public static void addItem(@NonNull Order order) {
        orderList.add(order);
    }

    public static void removeItem(int index) {
        orderList.remove(index);
    }

    public static void clearItems() {
        orderList.clear();
    }

    public static void insertItem(@NonNull Order order, int position) {
        orderList.set(position, order);
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
            orderId.setText(order.getOrderId());
            menu.setText(order.getOrderMenuList());
            totalPrice.setText("Total: "
                    + (""+order.getTotalPrice()).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")
                    + context.getResources().getString(R.string.krw));
        }
    }
}
