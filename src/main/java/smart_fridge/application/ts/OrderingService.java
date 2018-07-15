package smart_fridge.application.ts;

import smart_fridge.application.DbContext;
import smart_fridge.application.rdg.Grocery;
import smart_fridge.application.rdg.Order;
import smart_fridge.application.rdg.SLItem;
import smart_fridge.application.rdg.ShoppingList;
import smart_fridge.application.rdg.finders.GroceryFinder;
import smart_fridge.application.rdg.finders.OrderFinder;
import smart_fridge.application.rdg.finders.SLItemFinder;
import smart_fridge.application.rdg.finders.ShoppingListFinder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderingService {
    private static final OrderingService INSTANCE = new OrderingService();

    public static OrderingService getInstance() {
        return INSTANCE;
    }

    public void handleOrdering(Timestamp usageTimestamp) throws SQLException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        DbContext.getConnection().setAutoCommit(false);
        try {
            List<ShoppingList> shoppingLists = ShoppingListFinder.getInstance().findAll();
            Set<Integer> SLIdsInOrders = OrderFinder.getInstance().findAll().stream().map(Order::getShoppingListId).collect(Collectors.toSet());
            for (ShoppingList sl : shoppingLists) {
                if (sl.getInsertionId() == null || sl.getInsertionId() == 0) {
                    if (!SLIdsInOrders.contains(sl.getId())) {
                        List<SLItem> shoppingListItems = SLItemFinder.getInstance().findAllSLItemsBySL(sl.getId());
                        float sum = 0;

                        for (SLItem item : shoppingListItems) {
                            Grocery grocery = GroceryFinder.getInstance().findById(item.getGroceryId());
                            sum += grocery.getPrice().floatValue() * item.getAmount().floatValue();
                        }

                        Order newOrder = new Order();
                        newOrder.setShoppingListId(sl.getId());
                        newOrder.setOrderDate(usageTimestamp);
                        newOrder.setSum(new BigDecimal(sum));
                        newOrder.insert();

                        System.out.println("Shopping list #" + sl.getId() + " is unpaired.");
                        System.out.println("Made an order with the shopping list items.");
                    }
                }
            }
            DbContext.getConnection().commit();
            System.out.println("Making the orders completed");
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
