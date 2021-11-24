package io.github.untactorder.data;

import java.util.ArrayList;

/**
 * 매뉴리스트 include 메뉴
 * @author 유채민
 */
public class MenuList<T extends Menu> extends ArrayList<Menu> {
    ArrayList<Menu> menuList;

    public void addMenu(Menu menu) {
        menuList.add(menu);
    }

    public void getMenu(int i) {
        menuList.get(i);
    }
}
