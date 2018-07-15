package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.Grocery;
import smart_fridge.application.rdg.finders.GroceryFinder;
import smart_fridge.application.ui.printers.GroceryPrinter;

import java.sql.SQLException;
import java.util.List;

public class CetegoryMenu extends MainMenu {

    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Grain products                 ◆");
        System.out.println("◆ 2.    Seasonings                     ◆");
        System.out.println("◆ 3.    Oils                           ◆");
        System.out.println("◆ 4.    Meat                           ◆");
        System.out.println("◆ 5.    Vegetables                     ◆");
        System.out.println("◆ 6.    Fruits                         ◆");
        System.out.println("◆ 7.    Plants                         ◆");
        System.out.println("◆ 8.    Soy products                   ◆");
        System.out.println("◆ 9.    Plant milk                     ◆");
        System.out.println("◆ 10.   Seafood                        ◆");
        System.out.println("◆ 11.   Chilli pepper pastes           ◆");
        System.out.println("◆ 12.   Beverage                       ◆");
        System.out.println("◆ 13.   Alcohol                        ◆");
        System.out.println("◆ 14.   All categories                 ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Showing products from the category: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
            case "13":
            case "14":
                List<Grocery> foundGroceries = getGroceries(option);
                if (foundGroceries == null) break;
                printGroceries(foundGroceries);
                break;
            case "x":
                exit();
                break;
            default: System.out.println("Unknown option"); break;
        }
    }

    private List<Grocery> getGroceries(String option) {
        try {
            List<Grocery> foundGroceries;
            if ("14".equals(option))
                foundGroceries = GroceryFinder.getInstance().findAll();
            else
                foundGroceries = GroceryFinder.getInstance().findAllByCategoryId(Integer.valueOf(option));
            if (!(foundGroceries == null || foundGroceries.isEmpty())) {
                return foundGroceries;
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        System.out.println("No grocery from the chosen category found.");
        return null;
    }

    private void printGroceries(List<Grocery> foundGroceries) {
        for (Grocery groceryByCategory : foundGroceries) {
            GroceryPrinter.getInstance().print(groceryByCategory);
        }
    }
}
