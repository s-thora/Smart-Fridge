package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class MenuItemFinder extends BaseFinder<MenuItem> {

    private static final MenuItemFinder INSTANCE = new MenuItemFinder();

    public static MenuItemFinder getInstance() { return INSTANCE; }

    private MenuItemFinder() { }

    public MenuItem findById(int id) throws SQLException {
        return findByInt("SELECT m.id, m.dish_name, m.recipe_content, m.cooking_time, m.price\n" +
                "FROM \"Menu\" AS m\n" +
                "WHERE id=?", id);
    }

    public List<MenuItem> findAvail() throws SQLException {
        // find available dishes according to the inventory, ordered by complexity (with greater number of grocery kinds needed)
        return findAll("with groupped as (\n" +
                "SELECT m.id, m.dish_name, m.recipe_content, m.cooking_time, m.price, count(g.category_id)\n" +
                "FROM \"Menu\" AS m\n" +
                "LEFT JOIN \"Recipe groceries\" AS rg ON m.id=rg.dish_id\n" +
                "LEFT JOIN \"Groceries\" as g on rg.grocery_id=g.id\n" +
                "group by m.id, m.dish_name, m.recipe_content, m.cooking_time, m.price, g.category_id\n" +
                "order by m.id\n" +
                "), by_complexity as (\n" +
                "select id, dish_name, recipe_content, cooking_time, price, count(id)\n" +
                "from groupped\n" +
                "group by id, dish_name, recipe_content, cooking_time, price\n" +
                "-- order by count desc\n" +
                "), inventory as (\n" +
                "SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "LEFT JOIN \"Groceries\" AS g ON i.grocery_id=g.id\n" +
                "left JOIN \"Registered items\" as ri on i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "order by i.id)\n" +
                "SELECT distinct(by_complexity.id), dish_name, recipe_content, cooking_time, by_complexity.price, by_complexity.count FROM\n" +
                "by_complexity\n" +
                "JOIN \"Recipe groceries\" AS rg2 ON by_complexity.id=rg2.dish_id\n" +
                "JOIN inventory AS i ON rg2.grocery_id=i.grocery_id\n" +
                "order by by_complexity.count desc;");
    }

    public List<MenuItem> findAll() throws SQLException {
        return findAll("SELECT * FROM \"Menu\"");
    }

    @Override
    protected MenuItem load(ResultSet r) throws SQLException {
        MenuItem mi = new MenuItem();

        mi.setId(r.getInt("id"));
        mi.setDishName(r.getString("dish_name"));
        mi.setRecipe(r.getString("recipe_content"));
        mi.setCookingTime(r.getString("cooking_time"));
        mi.setPrice(r.getBigDecimal("price"));
        return mi;
    }

}