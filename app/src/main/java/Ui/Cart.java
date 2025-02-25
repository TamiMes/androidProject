package Ui;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;


public class Cart {
    private Map<String, Integer> items;
    private Map<String, Boolean> favorites;
    private String email;

    public Cart(String email) {
        this.items = new HashMap<>();
        this.favorites = new HashMap<>();
        this.email = email;
    }

    public Cart() {
    }

    public void addItem(String item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public int getQuantity(String item) {
        if (items == null) return 0;
        else
            return items.getOrDefault(item, 0);
    }

    public Map<String, Integer> getItems() {
        return new HashMap<>(items);
    }

    public Map<String, Boolean> getFavorites() { return new HashMap<>(favorites); }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFavorite(String string) {
        if (favorites == null) return false;
        else
            return favorites.getOrDefault(string, false);
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }
}
