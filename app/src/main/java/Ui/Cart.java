package Ui;

import android.util.Pair;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<String, Integer> items;
    private Map<String, Boolean> favorites;
    private Map<String, Map<String, Integer>> ratings;
    private String email;

    public Cart(String email) {
        this.items = new HashMap<>();
        this.favorites = new HashMap<>();
        this.ratings = new HashMap<>();
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
        return items.getOrDefault(item, 0);
    }

    public Map<String, Integer> getItems() {
        return new HashMap<>(items);
    }

    public Map<String, Boolean> getFavorites() {
        return new HashMap<>(favorites);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFavorite(String string) {
        if(favorites==null)return false;
        return favorites.getOrDefault(string, false);
    }

    public void setFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public void addRating(String item, String userEmail, int rating) {
        ratings.putIfAbsent(item, new HashMap<>());
        ratings.get(item).put(userEmail, rating);
    }

    public double getAverageRating(String item) {
        if (!ratings.containsKey(item)) return 0.0;
        int sum = 0, count = 0;
        for (int rate : ratings.get(item).values()) {
            sum += rate;
            count++;
        }
        return count == 0 ? 0.0 : (double) sum / count;
    }
}
