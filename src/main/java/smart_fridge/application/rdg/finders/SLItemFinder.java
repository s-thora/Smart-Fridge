package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.SLItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class SLItemFinder extends BaseFinder<SLItem> {

    private static final SLItemFinder INSTANCE = new SLItemFinder();

    public static SLItemFinder getInstance() {
        return INSTANCE;
    }

    private SLItemFinder() {
    }

    public List<SLItem> findAllSLItems() throws SQLException {
        return findAll("SELECT sl.id, sli.grocery_id, g.grocery_name, c.category_name, sli.amount, g.price, sl.insertion_id, c.measurement_unit\n" +
                "FROM \"Shopping lists\" AS sl\n" +
                "JOIN \"Shopping list items\" AS sli ON sl.id=sli.shopping_list_id\n" +
                "JOIN \"Groceries\" AS g ON sli.grocery_id=g.id\n" +
                "JOIN \"Categories\" AS c ON g.category_id=c.id;");
    }

    public SLItem findSLItemByGroceryId(Integer groceryId) throws SQLException {
        return findByInt("SELECT sl.id, sli.grocery_id, g.grocery_name, c.category_name, sli.amount, g.price, sl.insertion_id, c.measurement_unit\n" +
                "FROM \"Shopping lists\" AS sl\n" +
                "JOIN \"Shopping list items\" AS sli ON sl.id=sli.shopping_list_id\n" +
                "JOIN \"Groceries\" AS g ON sli.grocery_id=g.id\n" +
                "JOIN \"Categories\" AS c ON g.category_id=c.id\n" +
                "WHERE sli.grocery_id = ?;", groceryId);
    }

    public List<SLItem> findAllSLItemsBySL(Integer slId) throws SQLException {
        // finds items from the required shopping list
        return findAllByInt("SELECT sl.id, sli.grocery_id, g.grocery_name, c.category_name, sli.amount, g.price, sl.insertion_id, c.measurement_unit\n" +
                "FROM \"Shopping lists\" AS sl\n" +
                "JOIN \"Shopping list items\" AS sli ON sl.id=sli.shopping_list_id\n" +
                "JOIN \"Groceries\" AS g ON sli.grocery_id=g.id\n" +
                "JOIN \"Categories\" AS c ON g.category_id=c.id\n" +
                "WHERE shopping_list_id=?;", slId);
    }

    @Override
    protected SLItem load(ResultSet r) throws SQLException {
        SLItem sli = new SLItem();

        sli.setSLId(r.getInt("id"));
        sli.setGroceryId(r.getInt("grocery_id"));
        sli.setGroceryName(r.getString("grocery_name"));
        sli.setCategoryName(r.getString("category_name"));
        sli.setAmount(r.getBigDecimal("amount"));
        sli.setPrice(r.getBigDecimal("price"));
        sli.setInsertionId(r.getInt("insertion_id"));
        sli.setUnit(r.getString("measurement_unit"));

        return sli;
    }
}