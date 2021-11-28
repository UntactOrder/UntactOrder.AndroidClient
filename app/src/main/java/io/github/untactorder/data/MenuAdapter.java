package io.github.untactorder.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> implements OnMenuItemClickListener {

    ArrayList<Menu> items = new ArrayList<>();

    OnMenuItemClickListener listener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_menu_item, viewGroup,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Menu item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Menu item){
        items.add(item);
    }

    public Menu getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Menu item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuname;
        TextView menuprice;
        TextView quantity;

        public ViewHolder(View view, final OnMenuItemClickListener listner){
            super(view);

            menuname = view.findViewById(R.id.menu_tx_name);
            menuprice = view.findViewById(R.id.menu_tx_price);
            quantity = view.findViewById(R.id.menu_tx_quantity);

            view.findViewById(R.id.menu_tx_minus).setOnClickListener(v -> {
                int count = Integer.parseInt((String) quantity.getText());
                if (count > 0) {
                    quantity.setText(""+(count-1));
                }
            });
            view.findViewById(R.id.menu_tx_plus).setOnClickListener(v -> {
                int count = Integer.parseInt((String) quantity.getText());
                if (count < Integer.MAX_VALUE) {
                    quantity.setText(""+(count+1));
                }
            });
        }

        public void setItem(Menu item){
            menuname.setText(item.getMenu());
            menuprice.setText(item.getPrice());
        }
    }


}
