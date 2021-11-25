package io.github.untactorder.data;

/**
 * 매뉴
 * @author 유채민
 */
public class Menu {
    final String menuName;
    final int price;

    Menu(String name, int price) {
        this.menuName = name;
        this.price = price;
    }
     public String getMenuName() {
        return this.menuName;
     }

     public String getPrice() {
        return Integer.toString(this.price);
     }

}
