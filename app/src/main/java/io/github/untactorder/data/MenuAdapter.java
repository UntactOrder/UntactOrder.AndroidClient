package io.github.untactorder.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;
import org.jetbrains.annotations.NotNull;
/*
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    protected final MenuList<Menu> items = new MenuList<>();

    public MenuViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_ordr_list, parent, false);
        itemView.setSelected(true);
        itemView.setEnabled(false);
        return new MenuAdapter.MenuViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull @NotNull MenuAdapter.MenuViewHolder holder, int position) {
        holder.setItem(items.get(position));
    }

    public int getItemCount() {
        return items.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        protected TextView menuName;
        protected TextView price;
        public MenuViewHolder(View itemView) {
            super(itemView);

            menuName = itemView.findViewById(R.id.menuName);
            price = itemView.findViewById(R.id.price);
        }

        public void setItem(Menu menu) {
            menuName = setText(menu.getMenuName());
            price = setText(Integer.toString(menu.price));
        }


    }

}

 */
