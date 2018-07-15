package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.MenuItem;

/*
 *inspired by shanki
 */

public class MenuItemPrinter {

    private static final MenuItemPrinter INSTANCE = new MenuItemPrinter();

    public static MenuItemPrinter getInstance() { return INSTANCE; }

    private MenuItemPrinter() { }

    public void print(MenuItem menuItem) {
        if (menuItem  == null) {
            throw new NullPointerException("menu item cannot be null");
        }

        System.out.print("id:               ");
        System.out.println(menuItem.getId());
        System.out.print("dish name:        ");
        System.out.println(menuItem.getDishName());
        System.out.print("recipe content:   ");
        System.out.println(menuItem.getRecipe());
        System.out.print("cooking time:     ");
        System.out.println(menuItem.getCookingTime());
        System.out.print("price:            ");
        System.out.println(menuItem.getPrice());
        System.out.println();
    }
}
