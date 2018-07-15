package smart_fridge.application.ui.menus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import smart_fridge.application.rdg.*;
import smart_fridge.application.rdg.finders.ItemFinder;
import smart_fridge.application.rdg.finders.MenuItemFinder;
import smart_fridge.application.rdg.finders.OrderFinder;
import smart_fridge.application.ui.printers.ItemPrinter;
import smart_fridge.application.ui.printers.MenuItemPrinter;
import smart_fridge.application.ui.printers.OrderPrinter;

/*
 *inspired by shanki
 */

public class MainMenu extends Menu {

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Open the Smart Fridge          ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 2.    View the available groceries   ◆");
        System.out.println("◆ 3.    View the available menu items  ◆");
        System.out.println("◆ 4.    View the missing items         ◆");
        System.out.println("◆ 5.    View the registered actions    ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 6.    View all the groceries         ◆");
        System.out.println("◆ 7.    View all the menu items        ◆");
        System.out.println("◆ 8.    View all the orders            ◆");
        System.out.println("◆ 9.    View all the shopping lists    ◆");
        System.out.println("◆ 10.   Show statistics                ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 11.   Create an entry                ◆");
        System.out.println("◆ 12.   Update an entry                ◆");
        System.out.println("◆ 13.   Remove an entry                ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ x.    Close                          ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":   open(); break;

            case "2":    listAvailGroceries(); break;
            case "3":    listAvailMenuItems(); break;
            case "4":    listMissingItems(); break;
            case "5":    listRegActions(); break;

            case "6":    listAllGroceries(); break;
            case "7":    listAllMenuItems(); break;
            case "8":    listAllOrders(); break;
            case "9":    listShoppingLists(); break;
            case "10":   showStats(); break;

            case "11":   createAnEntry(); break;
            case "12":   updateAnEntry(); break;
            case "13":   removeAnEntry(); break;

            case "x":    exit(); break;
            default:     System.out.println("Unknown option"); break;
        }
    }

    private void open() { // 1
        OpenMenu openMenu = new OpenMenu();
        openMenu.run();
    }

    static void listAvailGroceries() { // 2
        try {
            List<Item> availGroceries = ItemFinder.getInstance().findAvail();
            if (availGroceries.isEmpty()) {
                System.out.println("The Smart Fridge is empty.");
                return;
            }
            for (Item availGrocery : availGroceries) {
                ItemPrinter.getInstance().print(availGrocery);
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }
    private void listAvailMenuItems () { // 3
        try {
            List<MenuItem> availMenuItems = MenuItemFinder.getInstance().findAvail();
            if (availMenuItems.isEmpty()) {
                System.out.println("No available menu items found.");
                return;
            }
            for (MenuItem availMenuItem : availMenuItems) {
                MenuItemPrinter.getInstance().print(availMenuItem);
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }

    private void listMissingItems() { // 4
        try {
            MissingItemsMenu miMenu = new MissingItemsMenu();
            miMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void listRegActions() { // 5
        try {
            RegisteredUsageMenu ruMenu = new RegisteredUsageMenu();
            ruMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    static void listAllGroceries() { // 6
        try {
            CetegoryMenu cetegoryMenu = new CetegoryMenu();
            cetegoryMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void listAllMenuItems() { // 7
        try {
            List<MenuItem> foundMenuItems = MenuItemFinder.getInstance().findAll();
            if (foundMenuItems.isEmpty()) {
                System.out.println("No menu items found.");
                return;
            }
            for (MenuItem menuItem : foundMenuItems) {
                MenuItemPrinter.getInstance().print(menuItem);
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }
    private static void listAllOrders() { // 8
        try {
            List<Order> foundOrders = OrderFinder.getInstance().findAll();
            if (foundOrders.isEmpty()) {
                System.out.println("No orders found.");
                return;
            }
            for (Order order : foundOrders) {
                OrderPrinter.getInstance().print(order);
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }

    private void listShoppingLists() { // 9
        try {
            ShoppingListMenu shoppingListMenu = new ShoppingListMenu();
            shoppingListMenu.run();

        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }
    private void showStats() { // 10
        try {
            StatsMenu statsMenu = new StatsMenu();
            statsMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void createAnEntry() { // 11
        try {
            CreateMenu createMenu = new CreateMenu();
            createMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void updateAnEntry() { // 12
        try {
            UpdateMenu updateMenu = new UpdateMenu();
            updateMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void removeAnEntry() { // 13
        try {
            DeleteMenu deleteMenu = new DeleteMenu();
            deleteMenu.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred:" + e.getMessage());
        }
    }

}