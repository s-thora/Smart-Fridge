package smart_fridge.application.ts;

import smart_fridge.application.DbContext;
import smart_fridge.application.rdg.Item;
import smart_fridge.application.rdg.RegisteredItem;
import smart_fridge.application.rdg.RegisteredUsage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EjectionService {
    private static final EjectionService INSTANCE = new EjectionService();

    public static EjectionService getInstance() {
        return INSTANCE;
    }

    public void handleEjection(List<Item> itemsToEject, RegisteredUsage registeredUsage) throws SQLException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);
        try {
            itemsToEject.forEach(e -> {
                try {
                    e.update();
                } catch (SQLException ex) {
                    System.out.println("A database error has occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            for (Item item : itemsToEject) {
                try {
                    RegisteredItem registeredItem = new RegisteredItem();
                    registeredItem.setGroceryId(item.getGroceryId());
                    registeredItem.setUsageId(registeredUsage.getId());
                    registeredItem.setAmount(item.getAmount());
                    registeredItem.setTypeId(2);
                    registeredItem.insert();
                } catch (SQLException e) {
                    System.out.println("A database error has occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            DbContext.getConnection().commit();
        } catch (SQLException e) {
            System.out.println("A database error has occurred: " + e.getMessage());
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("Transaction failed. Rollback");
            e.printStackTrace();
            DbContext.getConnection().rollback();
            throw e;
        } finally {
            DbContext.getConnection().setAutoCommit(true);
        }
    }
}
