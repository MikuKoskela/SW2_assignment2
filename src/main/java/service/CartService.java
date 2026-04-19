package service;

import model.Item;
import repository.CartRepository;

import java.util.List;

public class CartService {

    private final CartRepository repository;

    // Constructor injection
    public CartService(CartRepository repository) {
        this.repository = repository;
    }

    public int saveCartRecord(int totalItems, double total, String languageCode) {
        return repository.saveCart(totalItems, total, languageCode);
    }

    public void saveCartItems(int cartId, List<Item> items) {
        repository.saveItems(cartId, items);
    }
}