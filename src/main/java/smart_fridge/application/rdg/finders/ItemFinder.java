package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class ItemFinder extends BaseFinder<Item> {

    private static final ItemFinder INSTANCE = new ItemFinder();

    public static ItemFinder getInstance() { return INSTANCE; }

    private ItemFinder() { }

    public Item findById(Integer id) throws SQLException {
        return findByInt("SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "LEFT JOIN \"Groceries\" AS g ON i.grocery_id=g.id\n" +
                "left JOIN \"Registered items\" as ri on i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where i.removed is null and ri.inventory_id is not null and ri.type_id=1\n" +
                "and i.id = ?;", id);
    }

    public List<Item> findAvail() throws SQLException {
        // returns registered and present in the fridge items
        return findAll("SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "LEFT JOIN \"Groceries\" AS g ON i.grocery_id=g.id\n" +
                "left JOIN \"Registered items\" as ri on i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "order by i.id");
    }

    public List<Item> findUsedUpItems() throws SQLException {
        // returns available items, amount of which is less than required
        return findAll("SELECT null as id, g.id as grocery_id, g.grocery_name, c.category_name, null as production_date, sum(i.amount) as amount, g.minimal_amount, null as added, null as removed, g.keeping_time, g.price, c.measurement_unit, null as usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "LEFT JOIN \"Groceries\" AS g ON i.grocery_id=g.id\n" +
                "left JOIN \"Registered items\" as ri on i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "group by g.id, c.category_name, c.measurement_unit\n" +
                "having (sum(i.amount) - g.minimal_amount < 0)\n" +
                "ORDER BY g.id;");
    }

    public List<Item> findSpoiledItems() throws SQLException {
        // retuns avaivable items after expiracy
        return findAll("SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Groceries\" AS g\n" +
                "RIGHT JOIN \"Inventory\" AS i ON g.id=i.grocery_id\n" +
                "LEFT JOIN \"Registered items\" as ri on i.grocery_id=ri.grocery_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where (i.production_date + g.keeping_time < now())\n" +
                "and i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "ORDER BY i.grocery_id");
    }

    public List<Item> findMissingItems() throws SQLException {
        // returns availble items, which are after expiracy or amount of which is less then required
        return findAll("SELECT null as id, grocery_id, grocery_name, category_name, null as production_date, MAX(amount) as amount, minimal_amount, null as added, null as removed, keeping_time, price, measurement_unit, null as usage_id FROM\n" +
                "(SELECT id, grocery_id, grocery_name, category_name, production_date, amount, minimal_amount, added, removed, keeping_time, price, measurement_unit, usage_id FROM\n" +
                "(SELECT i.id, g.id as grocery_id, g.grocery_name, c.category_name, i.production_date, i.amount, g.minimal_amount, i.added, i.removed, g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Groceries\" AS g\n" +
                "RIGHT JOIN \"Inventory\" AS i ON g.id=i.grocery_id\n" +
                "LEFT JOIN \"Registered items\" as ri on i.grocery_id=ri.grocery_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where (i.production_date + g.keeping_time < now())\n" +
                "and i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "ORDER BY i.grocery_id\n" +
                ") AS spoiled_items\n" +
                "UNION ALL\n" +
                "SELECT null as id, grocery_id, grocery_name, category_name, null as production_date, amount, minimal_amount, null as added, null as removed, keeping_time, price, measurement_unit, null as usage_id FROM\n" +
                "(SELECT null as id, g.id as grocery_id, g.grocery_name, c.category_name, null as production_date, sum(i.amount) as amount, g.minimal_amount, null as added, null as removed, g.keeping_time, g.price, c.measurement_unit, null as usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "LEFT JOIN \"Groceries\" AS g ON i.grocery_id=g.id\n" +
                "left JOIN \"Registered items\" as ri on i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Categories\" as c ON g.category_id=c.id\n" +
                "where i.removed is null and i.added is not null and ri.inventory_id is not null and ri.type_id=1 and ri.usage_id != 0\n" +
                "group by g.id, c.category_name, c.measurement_unit\n" +
                "having (sum(i.amount) - g.minimal_amount < 0)\n" +
                "ORDER BY g.id\n" +
                ") AS used_up_items\n" +
                ") AS lacking_items\n" +
                "GROUP BY lacking_items.grocery_id, lacking_items.grocery_name, lacking_items.category_name, lacking_items.minimal_amount,\n" +
                "lacking_items.keeping_time, lacking_items.price, lacking_items.measurement_unit\n" +
                "ORDER BY grocery_id;");
    }

    public List<Item> findLackingItems() throws SQLException {
        // returns items, which were registered, but are not present in the fridge
        return findAll("SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name, \n" +
                "i.production_date, i.amount, g.minimal_amount, i.added, i.removed,\n" +
                "g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "from \"Registered items\" as ri\n" +
                "full join \"Inventory\" as i on ri.inventory_id=i.id\n" +
                "join \"Groceries\" as g on ri.grocery_id=g.id\n" +
                "join \"Categories\" as c on g.category_id=c.id\n" +
                "where i.id is null;");
    }

    public List<Item> findExcessingItems() throws SQLException {
        // returns items, which are present in the fridge, but were not registered
        return findAll("SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name, \n" +
                "i.production_date, i.amount, g.minimal_amount, i.added, i.removed,\n" +
                "g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "FROM \"Inventory\" AS i\n" +
                "FULL JOIN \"Registered items\" AS ri ON i.id=ri.inventory_id\n" +
                "LEFT JOIN \"Groceries\" AS g ON ri.grocery_id=g.id\n" +
                "LEFT JOIN \"Categories\" AS c ON g.category_id=c.id\n" +
                "WHERE i.removed IS NULL\n" +
                "AND ri.inventory_id IS NULL\n" +
                "AND ri.usage_id IS NULL;");
    }

    public List<Item> findIrregularItems() throws SQLException {
        // returns items, which were registered, but are not present in the fridge or
        // which are present in the fridge, but were not registered
        return findAll("select * from (\n" +
                "SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,\n" +
                "i.production_date, i.amount, g.minimal_amount, i.added, i.removed,\n" +
                "g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "from \"Registered items\" as ri\n" +
                "full join \"Inventory\" as i on ri.inventory_id=i.id\n" +
                "join \"Groceries\" as g on ri.grocery_id=g.id\n" +
                "join \"Categories\" as c on g.category_id=c.id\n" +
                "where i.id is null\n" +
                ") as lacking\n" +
                "union all\n" +
                "select * from (\n" +
                "SELECT ri.inventory_id, i.id, i.grocery_id, g.grocery_name, c.category_name,\n" +
                "i.production_date, i.amount, g.minimal_amount, i.added, i.removed,\n" +
                "g.keeping_time, g.price, c.measurement_unit, ri.usage_id\n" +
                "from \"Registered items\" as ri\n" +
                "full join \"Inventory\" as i on ri.inventory_id=i.id\n" +
                "left join \"Groceries\" as g on i.grocery_id=g.id\n" +
                "join \"Categories\" as c on g.category_id=c.id\n" +
                "where usage_id is null) \n" +
                "as excessing;");
    }

    @Override
    protected Item load(ResultSet r) throws SQLException {
        Item i = new Item();

        i.setId(r.getInt("id"));
        i.setGroceryId(r.getInt("grocery_id"));
        i.setGroceryName(r.getString("grocery_name"));
        i.setCategoryName(r.getString("category_name"));
        i.setProductionDate(r.getTimestamp("production_date"));
        i.setAmount(r.getBigDecimal("amount"));
        i.setMinimalAmount(r.getBigDecimal("minimal_amount"));
        i.setAdded(r.getTimestamp("added"));
        i.setRemoved(r.getTimestamp("removed"));
        i.setKeepingTime(r.getInt("keeping_time"));
        i.setPrice(r.getBigDecimal("price"));
        i.setUsageId(r.getInt("usage_id"));
        i.setUnit(r.getString("measurement_unit"));

        return i;
    }
}