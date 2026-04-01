import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Item> cart = new ArrayList<>();
    private double totalValue = 0;

    public void addItem(Item i){
        cart.add(i);
        totalValue += i.getValue()*i.getQuantity();
    }

    public void removeItem(Item i){
        cart.remove(i);
        totalValue -= i.getValue()*i.getQuantity();
    }
    public List<Item> getItems(){
        return cart;
    }

    public double getTotalValue() {
        return totalValue;
    }
}
