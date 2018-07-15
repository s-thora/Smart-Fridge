package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.StatsExpenses;
import smart_fridge.application.rdg.finders.StatsExpensesFinder;
import smart_fridge.application.ui.printers.StatsExpensesPrinter;
import java.sql.SQLException;
import java.util.List;

public class StatsExpensesMenu extends StatsMenu {
    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Group by month                 ◆");
        System.out.println("◆ 2.    Group by year                  ◆");
        System.out.println("◆ 3.    Show all the expenses          ◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose expenses statistics option: ");
    }

    @Override
    public void handle(String option) {
        switch (option) {
            case "1":
            case "2":
            case "3":
                List<StatsExpenses> foundItems = getItems(option);
                if (foundItems == null) {
                    System.out.println("No statistics data found");
                    break;
                }
                printItems(foundItems);
                break;
            case "x":
                exit();
                break;
            default:
                System.out.println("Unknown option");
                break;
        }
    }

    private List<StatsExpenses> getItems(String option) {
        try {
            List<StatsExpenses> foundItems = null;
            switch (option) {
                case "1": foundItems = StatsExpensesFinder.getInstance().findByMonth(); break;
                case "2": foundItems = StatsExpensesFinder.getInstance().findByYear(); break;
                case "3": foundItems = StatsExpensesFinder.getInstance().findAll(); break;
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

    private void printItems(List<StatsExpenses> foundItems) {
        for (StatsExpenses item : foundItems) {
            StatsExpensesPrinter.getInstance().print(item);
        }
    }
}
