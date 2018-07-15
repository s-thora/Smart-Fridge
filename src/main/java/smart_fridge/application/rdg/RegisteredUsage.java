package smart_fridge.application.rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/*
 *inspired by shanki
 */

public class RegisteredUsage extends BaseGateway {

    private Integer id;
    private Timestamp usageTimestamp;
    private String username;
    private String type;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Timestamp getUsageTimestamp() {
        return usageTimestamp;
    }
    public void setUsageTimestamp(Timestamp usageTimestamp) {
        this.usageTimestamp = usageTimestamp;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void insert() throws SQLException {
        if (id != null) {
            throw new IllegalStateException("id has been set");
        }

        insert("INSERT INTO \"Usage registration\" " +
                "(usage_timestamp, user_name) VALUES (?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setTimestamp(1, usageTimestamp);
        s.setString(2, username);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        id = r.getInt(1);
    }

    @Override
    protected void updateFill(PreparedStatement s) {
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Usage registration\" WHERE id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
    }

}