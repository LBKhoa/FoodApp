package khoalb.ntu.foodappoder.Helper;

import java.util.ArrayList;
import java.util.List;

public class CartHelper {
    private List<Item> items;

    public CartHelper() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String itemId;
        private int quantity;

        public Item() {
            // Default constructor required for calls to DataSnapshot.getValue(Item.class)
        }

        public Item(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}

