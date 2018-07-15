package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DeleteMenu extends MainMenu {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Remove a grocery              ◆");
        System.out.println("◆ 2.    Remove a menu item            ◆");
        System.out.println("◆ 3.    Remove a shopping list        ◆");
        System.out.println("◆ x.    Back                          ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose remove option: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
                geleteGrocery();
                break;
            case "2":
                deleteMenuItem();
                break;
            case "3":
                deleteSL();
                break;
            case "x":
                exit();
                break;
            default: System.out.println("Unknown option"); break;
        }
    }

    private void geleteGrocery() {
        try {
            Grocery delGrocery = InputReader.getGroceryById(br);
            if (delGrocery == null) {
                System.out.println("No such grocery exists.");
                return;
            }
            delGrocery.delete();
            System.out.println("The grocery was successfully deleted");
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }

    private void deleteMenuItem() {
        try {
            MenuItem delMenuItem = InputReader.getMenuItemById(br);
            if (delMenuItem == null) {
                System.out.println("No such grocery exists.");
                return;
            }
            delMenuItem.delete();
            System.out.println("The grocery was successfully deleted");
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }

    private void deleteSL() {
        try {
            ShoppingList delSL = InputReader.getSLById(br);
            if (delSL == null) {
                System.out.println("No such sholling list exists.");
                return;
            }
            Integer slId = delSL.getId();
            delSL.delete();

            System.out.println("All the shopping list " + slId + " items were successfully deleted.");
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }
}
