package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.StatsExpiracy;

public class StatsExpiracyPrinter {
    private static final StatsExpiracyPrinter INSTANCE = new StatsExpiracyPrinter();

    public static StatsExpiracyPrinter getInstance() { return INSTANCE; }

    private StatsExpiracyPrinter() { }

    public void print(StatsExpiracy se) {
        if (se  == null) {
            throw new NullPointerException("stats expiracy item cannot be null");
        }

        System.out.print("month:                ");
        System.out.println(se.getMonth());

        System.out.print("category name:        ");
        System.out.println(se.getCategoryName());

        System.out.print("ratio:                ");
        System.out.println(se.getRatio());
        System.out.println();
    }
}
