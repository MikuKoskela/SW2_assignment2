

import java.util.*;


public class Main {
    public static void main(String[] args){

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the language");
            String language = sc.nextLine();
            System.out.println("Enter the country");
            String country= sc.nextLine();
            Locale locale = new Locale(language, country);

        // 6. Implement localization so that the program can display messages in Finnish, Swedish, and English based
        // on the user's language selection
            ResourceBundle r =
                    ResourceBundle.getBundle("bundle", locale);

            System.out.println(r.getString("greetings"));

            //1.  Prompt the user to enter the number of items they want to purchase
            System.out.println(r.getString("totalAmount"));
            int totalAmount = sc.nextInt();

            ShoppingCart sp = new ShoppingCart();
            List<Item> list = new ArrayList<>();
            //2. For each item, ask the user for the price and quantity
            for(int i= 0; totalAmount > i; i++){
                System.out.println(r.getString("itemPrice"));
                int itemPrice = sc.nextInt();
                System.out.println(r.getString("itemAmount"));
                int itemAmount = sc.nextInt();
                sp.addItem(new Item(itemPrice,itemAmount));
            }

            //3. Calculate the total cost of each item (price × quantity).
            Calculator calculator = new Calculator();
            calculator.totalCost(list);

            //4. Calculate the total cost of all items in the shopping cart
            calculator.shoppingCartValue(sp);

            //5. Display the total cost of the shopping cart to the user.
            System.out.println(r.getString("totalCost")+ " " + calculator.shoppingCartValue(sp));
            System.out.println();
            System.out.println(r.getString("farewell"));
            System.out.println();
    }

}
