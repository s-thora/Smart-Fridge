package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.RegisteredUsage;
import smart_fridge.application.rdg.finders.RegisteredUsageFinder;
import smart_fridge.application.ui.printers.RegisteredUsagePrinter;

import java.sql.SQLException;
import java.util.List;

public class RegisteredUsageMenu extends MainMenu {
    @Override
    public void print() {
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("◆ 1.    Show registered insertions     ◆");
        System.out.println("◆ 2.    Show registered ejections      ◆");
        System.out.println("◆ 3.    Show all the registered actions◆");
        System.out.println("◆ x.    Back                           ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose registered action type: ");
    }

    @Override
    public void handle(String option) {
        List<RegisteredUsage> registeredUsages;
        switch (option) {
            case "1":
            case "2":
            case "3":
                registeredUsages = getRegUsages(option);
                if (registeredUsages == null) break;
                printRegisteredUsages(registeredUsages);
                break;
            case "x":
                exit();
                break;
            default: System.out.println("Unknown option"); break;
        }
    }

    private List<RegisteredUsage> getRegUsages(String option) {
        try {
            List<RegisteredUsage> registeredUsages = null;
            switch (option) {
                case "1": registeredUsages = RegisteredUsageFinder.getInstance().findAllInsertions(); break;
                case "2": registeredUsages = RegisteredUsageFinder.getInstance().findEjections(); break;
                case "3": registeredUsages = RegisteredUsageFinder.getInstance().findAll(); break;
            }
            if (!(registeredUsages == null || registeredUsages.isEmpty())) {
                return registeredUsages;
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        System.out.println("No registered actions found.");
        return null;
    }

    private void printRegisteredUsages(List<RegisteredUsage> registeredUsages) {
        for (RegisteredUsage registeredUsage : registeredUsages) {
            RegisteredUsagePrinter.getInstance().print(registeredUsage);
        }
    }
}
