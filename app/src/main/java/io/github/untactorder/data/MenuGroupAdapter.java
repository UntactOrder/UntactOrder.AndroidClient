package io.github.untactorder.data;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MenuGroupAdapter extends RecyclerView.Adapter<MenuGroupAdapter.MenuGroupViewHolder> {
    protected static Map<String, ArrayList<Menu>> menuGroup = new HashMap<>();
    protected static ArrayList<String> categories = new ArrayList<>();
    protected static String RECM_MENU;
    protected static MenuGroupAdapter self;
    protected static MenuAdapter menuAdapter;

    public MenuGroupAdapter(MenuAdapter adapter) {
        menuAdapter = adapter;
        self = this;
    }

    public static void notifyChanged() {
        if (self != null) self.notifyDataSetChanged();
        if (menuAdapter != null) menuAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_menu_group, viewGroup,false);
        return new MenuGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuGroupViewHolder viewHolder, int position) {
        String category = categories.get(position);
        viewHolder.setItem(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static void setMenuGroup(Map<String, ArrayList<Menu>> newMenuGroup) {
        menuGroup = newMenuGroup;
        categories.clear();
        for (String key: newMenuGroup.keySet()) {
            if (isRecmCategory(key)) {
                categories.add(0, key);
            } else {
                categories.add(key);
            }
        }
        changeCurrentViewingMenuList(categories.get(0));
    }

    public static void setMenuGroupFromMap(Map<String, Map<String, Object>> menus) {
        menuGroup = new HashMap<>();
        if (RECM_MENU != null) {
            addMenuList(RECM_MENU, new ArrayList<>());
        }
        menus.forEach((id, obj) -> {
            String name = (String) obj.get("name");
            int price = ((Double) obj.get("price")).intValue();
            String type = (String) obj.get("type");
            boolean pinned = ((Double) obj.get("pinned")).intValue() == 1;
            Menu menu = new Menu(id, name, price);
            if (!menuGroup.containsKey(type)) {
                addMenuList(type, new ArrayList<>());
            }
            if (RECM_MENU != null && pinned) {
                menuGroup.get(RECM_MENU).add(menu);
            }
            menuGroup.get(type).add(menu);
        });
        setMenuGroup(menuGroup);
    }

    public static void addMenuList(String category, ArrayList<Menu> menuList) {
        if (!menuGroup.containsKey(category)) {
            menuGroup.put(category, menuList);
            if (isRecmCategory(category)) {
                categories.add(0, category);
            } else {
                categories.add(category);
            }
        }
    }

    public static Menu findMenuById(String Id) {
        if (menuGroup != null) {
            for (Map.Entry<String, ArrayList<Menu>> entry : menuGroup.entrySet()) {
                for (Menu menu : entry.getValue()) {
                    if (menu.getId().equals(Id)) {
                        return menu;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isRecmCategory(String key) {
        return key.equals(RECM_MENU);
    }

    public static boolean isCurrentCategory(String key) {
        return key.equals(MenuAdapter.getCurrentViewingMenuCategory());
    }

    public static void setRecmMenuCategoryName(String name) {
        RECM_MENU = name;
    }

    public static void changeCurrentViewingMenuList(String category) {
        if (MenuAdapter.getCurrentViewingMenuCategory().equals(category)) return;
        MenuAdapter.setCurrentViewingMenuItemList(category, menuGroup.get(category));
        notifyChanged();
    }

    public static Map<String, String> makeOrderMap() {
        Map<String, String> orderMap = new HashMap<>();
        menuGroup.forEach((category, list) -> {
            for (Menu menu : list) {
                if (menu.getQuantity() > 0) {
                    orderMap.put(menu.getId(), "" + menu.getPrice());
                    menu.setQuantityToZero();
                }
            }
        });
        return orderMap;
    }

    static class MenuGroupViewHolder extends RecyclerView.ViewHolder{
        Drawable TRIANGLE_REVERSED, TRIANGLE;
        TextView directionView, categoryView;

        public MenuGroupViewHolder(View view){
            super(view);

            directionView = view.findViewById(R.id.direction);
            categoryView = view.findViewById(R.id.categori);

            TRIANGLE = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_triangle);
            TRIANGLE_REVERSED = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_reverse_triangle);

            view.findViewById(R.id.cat).setOnClickListener(v -> {
                changeCurrentViewingMenuList(categories.get(getAdapterPosition()));
                directionView.setBackground(TRIANGLE_REVERSED);
                notifyChanged();
            });
        }

        public void setItem(String category) {
            categoryView.setText(category);
            if (isCurrentCategory(category)) {
                directionView.setBackground(TRIANGLE_REVERSED);
            } else {
                directionView.setBackground(TRIANGLE);
            }
        }
    }
}
