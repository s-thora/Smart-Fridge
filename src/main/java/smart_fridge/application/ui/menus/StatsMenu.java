package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.StatsExpiracy;
import smart_fridge.application.rdg.finders.StatsExpiracyFinder;
import smart_fridge.application.ui.printers.StatsExpiracyPrinter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StatsMenu extends MainMenu {
    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Show inventarisation statistics◆");
        System.out.println("◆ 2.    Show expenses statistics       ◆");
        System.out.println("◆ 3.    Show product waste ratio       ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose statistics option: ");
    }


    @Override
    public void handle(String statsOption) {
        try {
            switch (statsOption) {
                case "1":
                    StatsInventoryMenu siMenu = new StatsInventoryMenu();
                    siMenu.run();
                    break;
                case "2":
                    StatsExpensesMenu seMenu = new StatsExpensesMenu();
                    seMenu.run();
                    break;
                case "3":
                    prodWasteRatio();
                    break;
                case "x":
                    exit();
                    break;
                default:
                    System.out.println("Unknown option");
                    break;
            }
        } catch (IOException e) {
            System.out.println("IO error has occurred: " + e.getMessage());
        }
    }

    private void prodWasteRatio() {
        try {
            List<StatsExpiracy> foundItems = StatsExpiracyFinder.getInstance().findAll();
            if (!foundItems.isEmpty()) {
                System.out.println("Product waste ratio for every month for every groceries category:");
                for (StatsExpiracy item : foundItems) {
                    StatsExpiracyPrinter.getInstance().print(item);
                }
            } else {
                System.out.println("No statistics data found");
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
    }

}
