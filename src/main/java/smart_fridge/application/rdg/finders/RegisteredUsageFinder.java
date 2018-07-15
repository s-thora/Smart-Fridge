package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.RegisteredUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class RegisteredUsageFinder extends BaseFinder<RegisteredUsage> {

    private static final RegisteredUsageFinder INSTANCE = new RegisteredUsageFinder();

    public static RegisteredUsageFinder getInstance() { return INSTANCE; }

    private RegisteredUsageFinder() { }

    public RegisteredUsage findById(int id) throws SQLException {
        return findByInt("SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name\n" +
                "FROM \"Usage registration\" AS ur\n" +
                "JOIN \"Registered items\" AS ri ON ur.id=ri.usage_id\n" +
                "JOIN \"Usage types\" AS ut ON ri.type_id=ut.id\n" +
                "WHERE ur.id=?\n" +
                "ORDER BY ur.usage_timestamp;", id);
    }

    public List<RegisteredUsage> findAllInsertions() throws SQLException {
        return findAll("SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name\n" +
                "FROM \"Usage registration\" AS ur\n" +
                "JOIN \"Registered items\" AS ri ON ur.id=ri.usage_id\n" +
                "JOIN \"Usage types\" AS ut ON ri.type_id=ut.id\n" +
                "WHERE ri.type_id=1\n" +
                "ORDER BY ur.usage_timestamp;");
    }

    public List<RegisteredUsage> findEjections() throws SQLException {
        return findAll("SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name\n" +
                "FROM \"Usage registration\" AS ur\n" +
                "JOIN \"Registered items\" AS ri ON ur.id=ri.usage_id\n" +
                "JOIN \"Usage types\" AS ut ON ri.type_id=ut.id\n" +
                "WHERE ri.type_id=2\n" +
                "ORDER BY ur.usage_timestamp;");
    }

    public List<RegisteredUsage> findAll() throws SQLException {
        return findAll("SELECT DISTINCT ur.id, ur.usage_timestamp, ut.type_name, ur.user_name\n" +
                "FROM \"Usage registration\" AS ur\n" +
                "JOIN \"Registered items\" AS ri ON ur.id=ri.usage_id\n" +
                "JOIN \"Usage types\" AS ut ON ri.type_id=ut.id\n" +
                "ORDER BY ur.usage_timestamp;");
    }

    @Override
    protected RegisteredUsage load(ResultSet r) throws SQLException {
        RegisteredUsage ru = new RegisteredUsage();

        ru.setId(r.getInt("id"));
        ru.setUsageTimestamp(r.getTimestamp("usage_timestamp"));
        ru.setUsername(r.getString("user_name"));
        ru.setType(r.getString("type_name"));

        return ru;
    }

}