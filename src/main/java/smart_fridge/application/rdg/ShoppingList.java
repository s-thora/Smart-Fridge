package smart_fridge.application.rdg;

import java.sql.*;

public class ShoppingList extends BaseGateway {

    private Integer id;
    private Integer insertionId;
    private Timestamp created;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getInsertionId() {
        return insertionId;
    }
    public void setInsertionId(Integer insertionId) {
        this.insertionId = insertionId;
    }
    public void setCreated(Timestamp created) {
        this.created = created;
    }


    public void insert() throws SQLException {
        if (id != null) {
            throw new IllegalStateException("id has been set");
        }

        insert("INSERT INTO \"Shopping lists\" " +
                "(created) VALUES (?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setTimestamp(1, created);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        id = r.getInt(1);
    }

    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        update("UPDATE \"Shopping lists\" " +
                "SET insertion_id = ?, created = ?" +
                "WHERE id = ?");
    }

    @Override
    protected void updateFill(PreparedStatement s) throws SQLException {
        s.setInt(1, insertionId);
        s.setTimestamp(2, created);
        s.setInt(3, id);
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Shopping lists\" WHERE id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
    }
}
