package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.Grocery;
import smart_fridge.application.rdg.MenuItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;

public class CreateMenu extends MainMenu {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Add a grocery                  ◆");
        System.out.println("◆ 2.    Add a menu item                ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose create option: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
                createGrocery();
                break;
            case "2":
                createMenuItem();
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

    private void createGrocery() {
        try {
            Grocery newGrocery = new Grocery();

            String groceryName = InputReader.readText(br, "Grocery name", 20);
            newGrocery.setGroceryName(groceryName);

            Integer categoryId = InputReader.readCategoryId(br);
            if (categoryId == null) return;
            newGrocery.setCategoryId(categoryId);

            Integer keepingTime = InputReader.readKeepingTime(br);
            if (keepingTime == null) return;
            newGrocery.setKeepingTime(keepingTime);

            BigDecimal minAmount = InputReader.readAmount(br, "minimum");
            if (minAmount == null) return;
            newGrocery.setMinimalAmount(minAmount);

            BigDecimal price = InputReader.readPrice(br);
            if (price == null) return;
            newGrocery.setPrice(price);


            newGrocery.insert();

            System.out.println("New grocery was successfully added.");
            System.out.println("New grocery ID is: ");
            System.out.println(newGrocery.getId());
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }

    private void createMenuItem() {
        try {
            MenuItem newMenuItem = new MenuItem();

            String dishName = InputReader.readText(br, "Dish name", 20);
            if (dishName == null) return;
            newMenuItem.setDishName(dishName);

            String recipe = InputReader.readText(br, "Recipe text", 100);
            if (recipe == null) return;
            newMenuItem.setRecipe(recipe);

            String cookingTime = InputReader.readCookingTime(br);
            if (cookingTime == null) return;
            newMenuItem.setCookingTime(cookingTime);

            BigDecimal price = InputReader.readPrice(br);
            if (price == null) return;
            newMenuItem.setPrice(price);

            newMenuItem.insert();

            System.out.println("New menu item was successfully added.");
            System.out.println("New menu item ID is: ");
            System.out.println(newMenuItem.getId());
        } catch (SQLException e) {
            System.out.println("A database error has occurred:" + e.getMessage());
        }
    }

}
