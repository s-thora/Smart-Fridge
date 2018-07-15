package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderFinder extends BaseFinder<Order> {

    private static final OrderFinder INSTANCE = new OrderFinder();

    public static OrderFinder getInstance() { return INSTANCE; }

    private OrderFinder() { }

    public List<Order> findAllById(int slID) throws SQLException {
        return findAllByInt("SELECT * FROM \"Orders\"\n" +
                "WHERE shopping_list_id=?;", slID);
    }

    public List<Order> findAll() throws SQLException {
        return findAll("SELECT * FROM \"Orders\";");
    }

    @Override
    protected Order load(ResultSet r) throws SQLException {
        Order o = new Order();

        o.setId(r.getInt("id"));
        o.setSum(r.getBigDecimal("sum"));
        o.setOrderDate(r.getTimestamp("order_date"));
        o.setShoppingListId(r.getInt("shopping_list_id"));

        return o;
    }

}
