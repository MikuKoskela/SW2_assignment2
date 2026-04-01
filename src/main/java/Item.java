public class Item {
    double price;
    int quantity;

    public Item(double price, int quantity){
        this.price = price;
        this.quantity = quantity;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public double getValue(){
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }
}
