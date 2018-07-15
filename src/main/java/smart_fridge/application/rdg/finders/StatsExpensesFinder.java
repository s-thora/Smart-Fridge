package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.StatsExpenses;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatsExpensesFinder extends BaseFinder<StatsExpenses> {
    private static final StatsExpensesFinder INSTANCE = new StatsExpensesFinder();

    public static StatsExpensesFinder getInstance() { return INSTANCE; }

    private StatsExpensesFinder() { }

    public List<StatsExpenses> findByMonth() throws SQLException {
        return findAll("SELECT  date_part('month', order_date) as month, null as year, SUM(sum) FROM \"Orders\"\n" +
                "GROUP BY month\n" +
                "ORDER BY month;");
    }

    public List<StatsExpenses> findByYear() throws SQLException {
        return findAll("SELECT null as month, date_part('year', order_date) as year, SUM(sum) FROM \"Orders\"\n" +
                "GROUP BY year\n" +
                "ORDER BY year;");
    }

    public List<StatsExpenses> findAll() throws SQLException {
        return findAll("SELECT null as month, null as year, SUM(sum) FROM \"Orders\";");
    }

    @Override
    protected StatsExpenses load(ResultSet r) throws SQLException {
        StatsExpenses se = new StatsExpenses();

        se.setMonth(r.getInt("month"));
        se.setYear(r.getInt("year"));
        se.setSum(r.getBigDecimal("sum"));

        return se;
    }
}
