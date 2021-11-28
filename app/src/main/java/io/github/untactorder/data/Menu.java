package io.github.untactorder.data;

public class Menu {
        String menu;
        String price;
        int quantity=0;

        public Menu(String menu, String price) {
            this.menu = menu;
            this.price = price;
        }

        public String getMenu() {
            return menu;
        }

        public void setMenu(String food_Name) {
            menu = food_Name;
        }

        public void setPrice(String price) {
            price = price;
        }

        public String getPrice() {
            return price;
        }
}
