package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.StatsExpiracy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatsExpiracyFinder extends BaseFinder<StatsExpiracy> {
    private static final StatsExpiracyFinder INSTANCE = new StatsExpiracyFinder();

    public static StatsExpiracyFinder getInstance() { return INSTANCE; }

    private StatsExpiracyFinder() { }

    public List<StatsExpiracy> findAll() throws SQLException {
        // returns ratio for amount of groceries ejected from the Smart Fridge being before and after expiration date
        // for each cateogry for every month within last 12 months
        return findAll("with being_spoiled as (\n" +
                "select date_part('month', i.removed) as month, category_name, count(*)::numeric\n" +
                "from \"Inventory\" as i\n" +
                "join \"Groceries\" as g on i.grocery_id=g.id\n" +
                "join \"Categories\" as c on g.category_id=c.id\n" +
                "join \"Registered items\" as ri on ri.inventory_id=i.id\n" +
                "join \"Usage registration\" as ur on ri.usage_id=ur.id\n" +
                "where (cast (concat(g.keeping_time, ' days') as interval) < (i.added - i.removed))\n" +
                "\tand i.removed > (now() - interval '12 months')\n" +
                "group by month, c.id\n" +
                "order by month, c.id\n" +
                "),\n" +
                "total as (\n" +
                "select date_part('month', i.removed) as month, category_name, count(*)::numeric\n" +
                "from \"Inventory\" as i\n" +
                "join \"Groceries\" as g on i.grocery_id=g.id\n" +
                "join \"Categories\" as c on g.category_id=c.id\n" +
                "join \"Registered items\" as ri on ri.inventory_id=i.id\n" +
                "join \"Usage registration\" as ur on ri.usage_id=ur.id\n" +
                "where i.removed > (now() - interval '12 months')\n" +
                "group by month, c.id\n" +
                "order by month, c.id)\n" +
                "select total.month, total.category_name, being_spoiled.count as exp_count, total.count as ttl_count\n" +
                "from total\n" +
                "full join being_spoiled on total.month=being_spoiled.month and total.category_name=being_spoiled.category_name\n" +
                "order by total.month;");
    }

    @Override
    protected StatsExpiracy load(ResultSet r) throws SQLException {
        StatsExpiracy se = new StatsExpiracy();

        se.setMonth(r.getInt("month"));
        se.setCategoryName(r.getString("category_name"));
        se.setTtlCount(r.getBigDecimal("ttl_count"));
        se.setExpCount(r.getBigDecimal("exp_count"));
        if (se.getExpCount() == null) se.setExpCount(BigDecimal.ZERO);
        se.setRatio(se.getExpCount().divide(se.getTtlCount(), new MathContext(5, RoundingMode.HALF_UP)));

        return se;
    }
}
