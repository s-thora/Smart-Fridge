package smart_fridge.application.ts;

import smart_fridge.application.DbContext;
import smart_fridge.application.rdg.Item;
import smart_fridge.application.rdg.RegisteredUsage;
import smart_fridge.application.rdg.SLItem;
import smart_fridge.application.rdg.ShoppingList;
import smart_fridge.application.rdg.finders.SLItemFinder;
import smart_fridge.application.rdg.finders.ShoppingListFinder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PairingService {
    private static final PairingService INSTANCE = new PairingService();

    public static PairingService getInstance() {
        return INSTANCE;
    }

    public void handlePairing(List<Item> insertedItems, RegisteredUsage registeredUsage) throws SQLException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);
        try {
            List<ShoppingList> shoppingLists = ShoppingListFinder.getInstance().findAll();
            for (ShoppingList sl : shoppingLists) {
                List<SLItem> shoppingListItems = SLItemFinder.getInstance().findAllSLItemsBySL(sl.getId());
                if (new HashSet<>(insertedItems.stream().map(Item::getGroceryId).collect(Collectors.toSet()))
                        .equals(new HashSet<>(shoppingListItems.stream().map(SLItem::getGroceryId).collect(Collectors.toSet())))) {
                    sl.setInsertionId(registeredUsage.getId());
                    sl.update();
                    System.out.println("Paired with shopping list #" + sl.getId() + " on insertion id: " + registeredUsage.getId());
                }
            }
            DbContext.getConnection().commit();
            System.out.println("Pairing the shopping lists with the inserted items completed");
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
