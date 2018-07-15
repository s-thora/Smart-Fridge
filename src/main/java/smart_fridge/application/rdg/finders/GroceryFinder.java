package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.Grocery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class GroceryFinder extends BaseFinder<Grocery> {

    private static final GroceryFinder INSTANCE = new GroceryFinder();

    public static GroceryFinder getInstance() { return INSTANCE; }

    private GroceryFinder() { }

    public Grocery findById(int id) throws SQLException {
        return findByInt("SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit\n" +
                "FROM \"Groceries\" as g\n" +
                "JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "WHERE g.id=?;", id);
    }

    public List<Grocery> findAllByCategoryId(int category_id) throws SQLException {
        return findAllByInt("SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit\n" +
                "FROM \"Groceries\" as g\n" +
                "JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "WHERE g.category_id=?;", category_id);
    }

    public List<Grocery> findAll() throws SQLException {
        return findAll("SELECT g.id, g.grocery_name, g.category_id, c.category_name, g.price, g.minimal_amount, g.keeping_time, c.measurement_unit\n" +
                "FROM \"Groceries\" as g\n" +
                "JOIN \"Categories\" as c ON g.category_id=c.id;");
    }

    @Override
    protected Grocery load(ResultSet r) throws SQLException {
        Grocery g = new Grocery();

        g.setId(r.getInt("id"));
        g.setGroceryName(r.getString("grocery_name"));
        g.setPrice(r.getBigDecimal("price"));
        g.setMinimalAmount(r.getBigDecimal("minimal_amount"));
        g.setKeepingTime(r.getInt("keeping_time"));
        g.setCategoryId(r.getInt("category_id"));
        g.setCategoryName(r.getString("category_name"));
        g.setUnit(r.getString("measurement_unit"));

        return g;
    }

}