package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.Item;

public class ItemPrinter {

    private static final ItemPrinter INSTANCE = new ItemPrinter();

    public static ItemPrinter getInstance() { return INSTANCE; }

    private ItemPrinter() { }

    public void print(Item item) {
        if (item  == null) {
            throw new NullPointerException("grocery cannot be null");
        }

        if (item.getId() != 0) {
            System.out.print("item id:           ");
            System.out.println(item.getId());
            System.out.print("grocery id:        ");
            System.out.println(item.getGroceryId());
            System.out.print("production date:   ");
            System.out.println(item.getProductionDate());
            System.out.print("added:             ");
            System.out.println(item.getAdded());
            if (item.getRemoved() != null) {
                System.out.print("removed:             ");
                System.out.println(item.getRemoved());
            }
        }
        if (item.getAmount() != null) {
            System.out.print("amount:            ");
            System.out.print(item.getAmount());
            if (item.getGroceryName() != null)
                System.out.println(" " + item.getUnit());
            else
                System.out.println();
        }
        if (item.getGroceryName() != null) {
            System.out.print("grocery name:      ");
            System.out.println(item.getGroceryName());
            System.out.print("category name:     ");
            System.out.println(item.getCategoryName());
            System.out.print("minimal amount:    ");
            System.out.println(item.getMinimalAmount() + " " + item.getUnit());
            System.out.print("keeping time:      ");
            System.out.println(item.getKeepingTime() + " days");
            System.out.print("price:             ");
            System.out.println(item.getPrice());
        }
        if (item.getUsageId() != null) {
            System.out.print("usage id:          ");
            System.out.println(item.getUsageId());
        }
        System.out.println();
    }
}
