package Ui;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<String, Integer> items;
    private String email;

    public Cart(String email) {
        this.items = new HashMap<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
