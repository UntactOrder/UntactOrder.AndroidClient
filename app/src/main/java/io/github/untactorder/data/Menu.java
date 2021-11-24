package io.github.untactorder.data;

/**
 * 매뉴
 * @author 유채민
 */
public class Menu {
    String menuName;
    int menuPrice;
    boolean pinned = true;

    Menu(String name, int menuPrice, boolean pinned) {
        this.menuName = name;
        this.menuPrice = menuPrice;
        this.pinned = pinned;
    }

}
