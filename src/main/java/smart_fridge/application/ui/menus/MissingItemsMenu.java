package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.*;
import smart_fridge.application.rdg.finders.GroceryFinder;
import smart_fridge.application.rdg.finders.ItemFinder;
import smart_fridge.application.ui.printers.ItemPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class MissingItemsMenu extends MainMenu {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Show used up items             ◆");
        System.out.println("◆ 2.    Show spoiled items             ◆");
        System.out.println("◆ 3.    Show all the missing items     ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Showing missing items: ");
    }

    @Override
    public void handle(String option) {
        List<Item> foundItems;
        switch (option) {
            case "1":
            case "2":
            case "3":
                foundItems = getMissingItems(option);
                if (foundItems == null) break;
                printMissingItems(foundItems);
                if (ifAddToSL()) {
                    addToSL(foundItems);
                }
                break;
            case "x":
                exit();
                break;
            default:
                System.out.println("Unknown option");
                break;
        }
    }

    private List<Item> getMissingItems(String option) {
        try {
            List<Item> foundItems = null;
            switch (option) {
                case "1": foundItems = ItemFinder.getInstance().findUsedUpItems(); break;
//                case "2": foundItems = getSpoiledItems(); break;
                case "2": foundItems = ItemFinder.getInstance().findSpoiledItems(); break;
                case "3": foundItems = ItemFinder.getInstance().findMissingItems(); break;
            }
            if (!(foundItems == null || foundItems.isEmpty())) {
                return foundItems;
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        System.out.println("No missing items found");
        return null;
    }

    private List<Item> getSpoiledItems() {
        // deprived
        List<Item> spoiledItems = null;
        try {
            spoiledItems = ItemFinder.getInstance().findAvail().stream()
                    // where (i.production_date + g.keeping_time < now())
                    .filter(e -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(e.getProductionDate());
                        try {
                            cal.add(Calendar.DAY_OF_WEEK, GroceryFinder.getInstance().findById(e.getGroceryId()).getKeepingTime());
                        } catch (SQLException e1) {
                            System.out.println("A database error has occurred: " + e1.getMessage());
                        }
                        Timestamp ts = new Timestamp(cal.getTime().getTime());
                        return ts.before(new Timestamp(System.currentTimeMillis()));
                    })
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        return spoiledItems;
    }

    private void printMissingItems(List<Item> foundItems) {
        for (Item item : foundItems) {
            ItemPrinter.getInstance().print(item);
        }
    }

    private boolean ifAddToSL() {
        try {
            while (true) {
                System.out.println("Add to the shopping list? (y/n)");
                switch (br.readLine()) {
                    case "y":
                        return true;
                    case "n":
                        return false;
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
        return false;
    }

    private void addToSL(List<Item> foundItems) {
        try {
            ShoppingList newSL = new ShoppingList();
            newSL.setCreated(new Timestamp(System.currentTimeMillis()));
            newSL.insert();
            Integer newSLId = newSL.getId();
            System.out.println("New shopping list " + newSLId + " was created");
            System.out.println("Adding the items to the shopping list...");

            for (Item mItem : foundItems) {
                Grocery grocery = GroceryFinder.getInstance().findById(mItem.getGroceryId());
                SLItem slItem = new SLItem();
                slItem.setSLId(newSLId);
                slItem.setGroceryId(mItem.getGroceryId());
                if (mItem.getAmount().compareTo(grocery.getMinimalAmount()) < 0) {
                    slItem.setAmount(grocery.getMinimalAmount().subtract(mItem.getAmount()));
                } else {
                    slItem.setAmount(grocery.getMinimalAmount());
                }

                slItem.insert();
            }
            System.out.println("Missing items were successfully added to the shopping list.");
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }
}
