package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.Order;

public class OrderPrinter {

    private static final OrderPrinter INSTANCE = new OrderPrinter();

    public static OrderPrinter getInstance() { return INSTANCE; }

    private OrderPrinter() { }

    public void print(Order order) {
        if (order  == null) {
            throw new NullPointerException("order cannot be null");
        }

        System.out.print("id:              ");
        System.out.println(order.getId());
        System.out.print("sum:             ");
        System.out.println(order.getSum());
        System.out.print("order date:      ");
        System.out.println(order.getOrderDate());
        System.out.print("shopping list:   ");
        System.out.println(order.getShoppingListId());
        System.out.println();
    }
}
