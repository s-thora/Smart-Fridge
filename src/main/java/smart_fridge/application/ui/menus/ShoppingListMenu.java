package smart_fridge.application.ui.menus;

import smart_fridge.application.rdg.SLItem;
import smart_fridge.application.rdg.finders.SLItemFinder;
import smart_fridge.application.ui.printers.SLItemPrinter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingListMenu extends MainMenu {

    private Map<Integer, List<SLItem>> SLById;

    @Override
    public void print() {
        SLById = getSLById();
        if (SLById == null || SLById.isEmpty()) {
            System.out.println("No shopping lists found.");
            exit();
        }

        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        for (Integer slId : SLById.keySet()) {
            System.out.println("◆ " + slId + ". View shopping list # " + slId + "            ◆");
        }
        System.out.println("◆ x. Back                              ◆");
        System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
        System.out.println("Choose shopping list: ");
    }

    @Override
    public void handle(String option) {
        if ("x".equals(option)) {
            exit();
            return;
        }

        try {
            Integer chosenId = Integer.parseInt(option);

            if (!SLById.containsKey(chosenId)) {
                System.out.println("Unknown option.");
                return;
            }

            if (SLById.get(chosenId).isEmpty()) {
                System.out.println("Chosen shopping list is empty.");
                return;
            }

            for (SLItem slItem : SLById.get(chosenId)) {
                SLItemPrinter.getInstance().print(slItem);
            }
        } catch (NumberFormatException e) {
            System.out.println("Unknown option.");
        }
    }

    private Map<Integer, List<SLItem>> getSLById() {
        try {
            List<SLItem> foundSLItems = SLItemFinder.getInstance().findAllSLItems();
            if (!(foundSLItems == null || foundSLItems.isEmpty())) {
                return foundSLItems.stream().collect(Collectors.groupingBy(SLItem::getSLId));
            }
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
        }
        System.out.println("No shopping lists found.");
        return null;
    }
}