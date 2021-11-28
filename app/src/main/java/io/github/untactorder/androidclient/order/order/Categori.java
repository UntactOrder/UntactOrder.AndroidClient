package io.github.untactorder.androidclient.order.order;

public class Categori {
    String categori;
    boolean selected;

    public Categori(String categori) {
        this.categori = categori;
        this.selected = false;
    }

    public String getCategori() {
        return categori;
    }

    public void setCategori(String food_Name) {
        categori = food_Name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
