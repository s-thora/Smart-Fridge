package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.ShoppingList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShoppingListFinder extends BaseFinder<ShoppingList> {
    private static final ShoppingListFinder INSTANCE = new ShoppingListFinder();

    public static ShoppingListFinder getInstance() { return INSTANCE; }

    private ShoppingListFinder() { }

    public ShoppingList findById(int id) throws SQLException {
        return findByInt("SELECT *\n" +
                "FROM \"Shopping lists\"\n" +
                "WHERE id=?;", id);
    }

    public List<ShoppingList> findAll() throws SQLException {
        return findAll("SELECT *\n" +
                "FROM \"Shopping lists\";");
    }

    @Override
    protected ShoppingList load(ResultSet r) throws SQLException {
        ShoppingList sl = new ShoppingList();

        sl.setId(r.getInt("id"));
        sl.setInsertionId(r.getInt("insertion_id"));

        return sl;
    }
}
