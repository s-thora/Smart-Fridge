package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/*
 *inspired by shanki
 */

public class Item extends Grocery {

    private Integer id;
    private Timestamp productionDate;
    private BigDecimal amount;
    private Timestamp added;
    private Timestamp removed;
    private Integer usageId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getGroceryId() {
        return groceryId;
    }
    public void setGroceryId(Integer groceryId) {
        this.groceryId = groceryId;
    }
    public Timestamp getProductionDate() {
        return productionDate;
    }
    public void setProductionDate(Timestamp productionDate) {
        this.productionDate = productionDate;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Timestamp getAdded() {
        return added;
    }
    public void setAdded(Timestamp added) {
        this.added = added;
    }
    public Timestamp getRemoved() {
        return removed;
    }
    public void setRemoved(Timestamp removed) {
        this.removed = removed;
    }
    public Integer getUsageId() {
        return usageId;
    }
    public void setUsageId(Integer usageId) {
        this.usageId = usageId;
    }

    public void insert() throws SQLException {
        insert("INSERT INTO \"Inventory\" " +
                "(grocery_id, production_date, added, removed, amount) VALUES (?,?,?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setInt(1, groceryId);
        s.setTimestamp(2, productionDate);
        s.setTimestamp(3, added);
        s.setTimestamp(4, removed);
        s.setBigDecimal(5, amount);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        id = r.getInt(1);
    }

    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        update("UPDATE \"Inventory\" " +
                "SET grocery_id = ?, production_date = ?, added = ?, removed = ?, amount = ?" +
                "WHERE id = ?");
    }

    @Override
    protected void updateFill(PreparedStatement s) throws SQLException {
        s.setInt(1, groceryId);
        s.setTimestamp(2, productionDate);
        s.setTimestamp(3, added);
        s.setTimestamp(4, removed);
        s.setBigDecimal(5, amount);
        s.setInt(6, id);
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Inventory\" WHERE id = ?");
    }


    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
    }
}