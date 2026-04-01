import java.util.List;

public class Calculator {

    public double totalCost(List<Item> items){
        double total = 0;
        for(Item item: items){
            total += item.getValue()*item.getQuantity();
        }
        return total;
    }

    public double shoppingCartValue(ShoppingCart cart){
        return cart.getTotalValue();
    }
}
