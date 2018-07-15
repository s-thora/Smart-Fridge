package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.*;
import smart_fridge.application.rdg.finders.GroceryFinder;
import smart_fridge.application.rdg.finders.SLItemFinder;
import smart_fridge.application.rdg.finders.ShoppingListFinder;
import smart_fridge.application.ts.EjectionService;
import smart_fridge.application.ts.InsertionService;
import smart_fridge.application.ts.OrderingService;
import smart_fridge.application.ts.PairingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class OpenMenu extends MainMenu {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private String username;
    private Timestamp usageTimestamp;
    private RegisteredUsage registeredUsage;

    @Override
    public void run() {
        try {
            System.out.println("Enter your username: ");
            this.username = br.readLine();
            this.usageTimestamp = new Timestamp(System.currentTimeMillis());
            this.registeredUsage = insertUsageReg();
            if (this.registeredUsage == null) exit();
            super.run();
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Insert items                   ◆");
        System.out.println("◆ 2.    Eject items                    ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose the action: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
                try {
                    List<Item> itemsToInsert = getItems("Insert");
                    if (itemsToInsert != null && !itemsToInsert.isEmpty()) {
                        InsertionService.getInstance().handleInsertion(itemsToInsert, registeredUsage);
                        System.out.println("Successfully inserted " + itemsToInsert.size() + " item(s)");
                        System.out.println("Pairing the insertion with shopping lists...");
                        PairingService.getInstance().handlePairing(itemsToInsert, registeredUsage);
                        System.out.println("Making an order with unpaired shopping lists...");
                        OrderingService.getInstance().handleOrdering(usageTimestamp);
                    } else {
                        System.out.println("No items were inserted");
                    }
                } catch (SQLException e) {
                    System.out.println("A database error has occurred: " + e.getMessage());
                }
                break;
            case "2":
                try {
                    List<Item> itemsToEject = getItems("Exect");
                    if (itemsToEject != null && !itemsToEject.isEmpty()) {
                        EjectionService.getInstance().handleEjection(itemsToEject, registeredUsage);
                        System.out.println("Successfully ejected " + itemsToEject.size() + " item(s)");
                    } else {
                        System.out.println("No items were ejected");
                    }
                } catch (SQLException e) {
                    System.out.println("A database error has occurred: " + e.getMessage());
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


    private List<Item> getItems(String action) {
        List<Item> items = new ArrayList<>();

        while (true) {
            try {
                System.out.println(action + " an item? (y/n)");
                if ("y".equals(br.readLine())) {
                    if ("Insert".equals(action)) {
                        Item itemToInsert = getItemToInsert();
                        if (itemToInsert != null) {
                            items.add(itemToInsert);
                        }
                    } else {
                        Item itemToEject = getItemToEject();
                        if (itemToEject != null) {
                            items.add(itemToEject);
                        }
                    }
                } else {
                    break;
                }
            } catch (IOException e) {
                System.out.println("IO error has occurred: " + e.getMessage());
            }
        }
        return items;
    }

    private Item getItemToInsert() {
        Item newItem = new Item();

        Grocery grocery;
        System.out.println("All the available groceries: ");
        MainMenu.listAllGroceries();
        grocery = InputReader.getGroceryById(br);
        if (grocery == null) return null;
        newItem.setGroceryId(grocery.getId());
        Timestamp productionDate = InputReader.readTimestamp(br);
        newItem.setProductionDate(productionDate);

        newItem.setAdded(usageTimestamp);
        BigDecimal amount = InputReader.readAmount(br, "inserted");
        newItem.setAmount(amount);

        return newItem;
    }

    private Item getItemToEject() {
        Item item;
        System.out.println("All the available grocery items: ");
        MainMenu.listAvailGroceries();
        item = InputReader.getItemById(br);
        item.setRemoved(usageTimestamp);
        return item;
    }

    private RegisteredUsage insertUsageReg() {
        RegisteredUsage registeredUsage = null;
        try {
            registeredUsage = new RegisteredUsage();
            registeredUsage.setUsageTimestamp(usageTimestamp);
            registeredUsage.setUsername(username);
            registeredUsage.insert();
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        return registeredUsage;
    }
}
