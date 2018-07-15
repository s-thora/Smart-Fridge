package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisteredItem extends BaseGateway {

    private Integer groceryId;
    private Integer inventoryId;
    private Integer usageId;
    private BigDecimal amount;
    private Integer typeId;

    public void setGroceryId(Integer groceryId) {
        this.groceryId = groceryId;
    }
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    public void setUsageId(Integer usageId) {
        this.usageId = usageId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    public Integer getInventoryId() { return this.inventoryId; }
    public Integer getTypeId() { return this.typeId; }
    public Integer getUsageId() { return this.usageId; }

    public void insert() throws SQLException {
        insert("INSERT INTO \"Registered items\" " +
                "(grocery_id, inventory_id, usage_id, amount, type_id) VALUES (?,?,?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setInt(1, groceryId);
        s.setInt(2, inventoryId);
        s.setInt(3, usageId);
        s.setBigDecimal(4, amount);
        s.setInt(5, typeId);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) {
    }

    @Override
    protected void updateFill(PreparedStatement s) {
    }

    @Override
    protected void deleteFill(PreparedStatement s) {
    }
}
