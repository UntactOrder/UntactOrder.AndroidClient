package io.github.untactorder.data;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    protected static ArrayList<Menu> currentViewingMenuItems = new ArrayList<>();
    protected static String currentViewingMenuCategory = "";

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_menu_list, viewGroup,false);
        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position) {
        Menu item = currentViewingMenuItems.get(position);
        viewHolder.setItem(item);
    }

    public static void setCurrentViewingMenuItemList(String category, ArrayList<Menu> itemList) {
        currentViewingMenuCategory = category;
        currentViewingMenuItems = itemList;
    }

    public static String getCurrentViewingMenuCategory() {
        return currentViewingMenuCategory;
    }

    @Override
    public int getItemCount() {
        return currentViewingMenuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView menuNameView, priceView, quantityView;

        @SuppressLint("SetTextI18n")
        public MenuViewHolder(View view){
            super(view);

            menuNameView = view.findViewById(R.id.menu_tx_name);
            priceView = view.findViewById(R.id.menu_tx_price);
            quantityView = view.findViewById(R.id.menu_tx_quantity);

            view.findViewById(R.id.menu_tx_minus).setOnClickListener(v ->
                    quantityView.setText(""+currentViewingMenuItems.get(getAdapterPosition()).decreaseQuantity()));
            view.findViewById(R.id.menu_tx_plus).setOnClickListener(v ->
                    quantityView.setText(""+currentViewingMenuItems.get(getAdapterPosition()).increaseQuantity()));
        }

        @SuppressLint("SetTextI18n")
        public void setItem(Menu item) {
            menuNameView.setText(item.getName());
            priceView.setText(""+item.getPrice());
            quantityView.setText(""+item.getQuantity());
        }
    }
}
