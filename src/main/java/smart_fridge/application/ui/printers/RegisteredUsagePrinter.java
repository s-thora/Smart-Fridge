package smart_fridge.application.ui.printers;

import smart_fridge.application.rdg.RegisteredUsage;

public class RegisteredUsagePrinter {

    private static final RegisteredUsagePrinter INSTANCE = new RegisteredUsagePrinter();

    public static RegisteredUsagePrinter getInstance() { return INSTANCE; }

    private RegisteredUsagePrinter() { }

    public void print(RegisteredUsage regUsage) {
        if (regUsage  == null) {
            throw new NullPointerException("registered usage cannot be null");
        }

        System.out.print("id:             ");
        System.out.println(regUsage.getId());
        System.out.print("usage time:     ");
        System.out.println(regUsage.getUsageTimestamp());
        System.out.print("usage type:     ");
        System.out.println(regUsage.getType());
        System.out.print("user:           ");
        System.out.println(regUsage.getUsername());
        System.out.println();
    }
}
