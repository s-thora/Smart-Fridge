package smart_fridge.application.ts;

/*
inspired by shanki
 */

import smart_fridge.application.DbContext;
import smart_fridge.application.rdg.Item;
import smart_fridge.application.rdg.RegisteredItem;
import smart_fridge.application.rdg.RegisteredUsage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InsertionService {
    private static final InsertionService INSTANCE = new InsertionService();

    public static InsertionService getInstance() {
        return INSTANCE;
    }

    public void handleInsertion(List<Item> itemsToInsert, RegisteredUsage registeredUsage) throws SQLException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);
        try {
            itemsToInsert.forEach(e -> {
                try {
                e.insert();
                } catch (SQLException ex) {
                    System.out.println("A database error has occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            for (Item item : itemsToInsert) {
                RegisteredItem registeredItem = new RegisteredItem();
                registeredItem.setGroceryId(item.getGroceryId());
                registeredItem.setInventoryId(item.getId());
                registeredItem.setUsageId(registeredUsage.getId());
                registeredItem.setAmount(item.getAmount());
                registeredItem.setTypeId(1);
                registeredItem.insert();
            }
            DbContext.getConnection().commit();
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
