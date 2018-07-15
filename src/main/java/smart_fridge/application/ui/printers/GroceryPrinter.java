package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.Grocery;

public class GroceryPrinter {

    private static final GroceryPrinter INSTANCE = new GroceryPrinter();

    public static GroceryPrinter getInstance() { return INSTANCE; }

    private GroceryPrinter() { }

    public void print(Grocery grocery) {
        if (grocery  == null) {
            throw new NullPointerException("grocery cannot be null");
        }

        System.out.print("id:              ");
        System.out.println(grocery.getId());
        System.out.print("grocery name:    ");
        System.out.println(grocery.getGroceryName());
        System.out.print("category id:     ");
        System.out.println(grocery.getCategoryId());
        System.out.print("category name:   ");
        System.out.println(grocery.getCategoryName());
        System.out.print("price:           ");
        System.out.println(grocery.getPrice());
        System.out.print("minimal amount:  ");
        System.out.println(grocery.getMinimalAmount() + " " + grocery.getUnit());
        System.out.print("keeping time:    ");
        System.out.println(grocery.getKeepingTime() + " days");
        System.out.println();
    }
}
