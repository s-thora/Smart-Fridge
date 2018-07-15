package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.Grocery;
import smart_fridge.application.rdg.MenuItem;
import smart_fridge.application.ui.printers.GroceryPrinter;
import smart_fridge.application.ui.printers.MenuItemPrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;

public class UpdateMenu extends MainMenu {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Update a grocery               ◆");
        System.out.println("◆ 2.    Update a menu item             ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose update option: ");
    }

    @Override
    public void handle(String option) {
            switch (option) {
                case "1":
                    updateGrocery();
                    break;
                case "2":
                    updateMenuItem();
                    break;
                case "3":
                    System.out.println("Not Implemented Yet.");
                    break;
                case "x":
                    exit();
                    break;
                default:
                    System.out.println("Unknown option");
                    break;
            }

    }

    private void updateGrocery() {
        try {
            Grocery updGrocery = InputReader.getGroceryById(br);
            if (updGrocery == null) {
                System.out.println("No such grocery exists.");
                return;
            }

            GroceryPrinter.getInstance().print(updGrocery);

            String groceryName = InputReader.readText(br, "Grocery name", 20);
            updGrocery.setGroceryName(groceryName);

            Integer categoryId = InputReader.readCategoryId(br);
            if (categoryId == null) return;
            updGrocery.setCategoryId(categoryId);

            Integer keepingTime = InputReader.readKeepingTime(br);
            if (keepingTime == null) return;
            updGrocery.setKeepingTime(keepingTime);

            BigDecimal minAmount = InputReader.readAmount(br, "minimum");
            if (minAmount == null) return;
            updGrocery.setMinimalAmount(minAmount);

            BigDecimal price = InputReader.readPrice(br);
            if (price == null) return;
            updGrocery.setPrice(price);

            updGrocery.update();
            System.out.println("The grocery was successfully updated");
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }

    private void updateMenuItem() {
        try {
            MenuItem updMenuItem = InputReader.getMenuItemById(br);
            if (updMenuItem == null) {
                System.out.println("No such menu item exists.");
                return;
            }

            MenuItemPrinter.getInstance().print(updMenuItem);

            String dishName = InputReader.readText(br, "Dish name", 20);
            if (dishName == null) return;
            updMenuItem.setDishName(dishName);

            String recipe = InputReader.readText(br, "Recipe text", 100);
            if (recipe == null) return;
            updMenuItem.setRecipe(recipe);

            String cookingTime = InputReader.readCookingTime(br);
            if (cookingTime == null) return;
            updMenuItem.setCookingTime(cookingTime);

            BigDecimal price = InputReader.readPrice(br);
            if (price == null) return;
            updMenuItem.setPrice(price);

            updMenuItem.update();
            System.out.println("The menu item was successfully updated");
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }
}
