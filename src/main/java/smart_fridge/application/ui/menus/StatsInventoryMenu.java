package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.Item;
import smart_fridge.application.rdg.finders.ItemFinder;
import smart_fridge.application.ui.printers.ItemPrinter;

import java.sql.SQLException;
import java.util.List;

public class StatsInventoryMenu extends StatsMenu {
    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Show lacking items             ◆");
        System.out.println("◆ 2.    Show excessing items           ◆");
        System.out.println("◆ 3.    Show all irregular items       ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose inventarisation option: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
            case "2":
            case "3":
                List<Item> foundItems = getItems(option);
                if (foundItems == null) break;
                printItems(option, foundItems);
                break;
            case "x":
                exit();
                break;
            default:
                System.out.println("Unknown option");
                break;
        }
    }

    private List<Item> getItems(String option) {
        try {
            List<Item> foundItems = null;
            switch (option) {
                case "1": foundItems = ItemFinder.getInstance().findLackingItems(); break;
                case "2": foundItems = ItemFinder.getInstance().findExcessingItems(); break;
                case "3": foundItems = ItemFinder.getInstance().findIrregularItems(); break;
            }
            if (!(foundItems == null || foundItems.isEmpty())) {
                return foundItems;
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        System.out.println("No irregular items found.");
        return null;
    }

    private void printItems(String option, List<Item> foundItems) {
        for (Item irregItem : foundItems) {
            switch (option) {
                case "1":
                    ItemPrinter.getInstance().print(irregItem);
                    break;
                case "2":
                    ItemPrinter.getInstance().print(irregItem);
                    break;
                case "3":
                    ItemPrinter.getInstance().print(irregItem);
                    break;
            }
        }
    }
}
