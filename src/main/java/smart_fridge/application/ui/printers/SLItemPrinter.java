package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.SLItem;

public class SLItemPrinter {

    private static final SLItemPrinter INSTANCE = new SLItemPrinter();

    public static SLItemPrinter getInstance() { return INSTANCE; }

    private SLItemPrinter() { }

    public void print(SLItem item) {
        if (item  == null) {
            throw new NullPointerException("grocery cannot be null");
        }

        System.out.print("grocery id:        ");
        System.out.println(item.getGroceryId());
        System.out.print("grocery name:      ");
        System.out.println(item.getGroceryName());
        System.out.print("category name:     ");
        System.out.println(item.getCategoryName());
        System.out.print("amount:            ");
        System.out.println(item.getAmount() + " " + item.getUnit());
        System.out.print("price:             ");
        System.out.println(item.getPrice());
        System.out.print("insertion id:      ");
        System.out.println(item.getInsertionId());
        System.out.println();
    }
}
