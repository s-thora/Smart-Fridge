package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.StatsExpenses;

public class StatsExpensesPrinter {
    private static final StatsExpensesPrinter INSTANCE = new StatsExpensesPrinter();

    public static StatsExpensesPrinter getInstance() { return INSTANCE; }

    private StatsExpensesPrinter() { }

    public void print(StatsExpenses se) {
        if (se  == null) {
            throw new NullPointerException("stats expenses item cannot be null");
        }

        if (se.getMonth() != 0) {
            System.out.print("month:          ");
            System.out.println(se.getMonth());
        }
        if (se.getYear() != 0) {
            System.out.print("year:           ");
            System.out.println(se.getYear());
        }
        System.out.print("sum:            ");
        System.out.println(se.getSum());
        System.out.println();
    }
}
