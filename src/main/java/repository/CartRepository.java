package repository;

import model.Item;

public interface CartRepository {

    int saveCart(int totalItems, double total, String languageCode);

    void saveItems(int cartId, java.util.List<Item> items);
}