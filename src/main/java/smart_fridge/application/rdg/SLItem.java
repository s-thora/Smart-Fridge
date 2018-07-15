package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *inspired by shanki
 */

public class SLItem extends BaseGateway {

    private Integer SLId;
    private Integer groceryId;
    private String groceryName;
    private String categoryName;
    private BigDecimal amount;
    private BigDecimal price;
    private Integer insertionId;
    private String unit;

    public Integer getSLId() { return SLId; }
    public void setSLId(Integer SLId) {
        this.SLId = SLId;
    }
    public Integer getGroceryId() {
        return groceryId;
    }
    public void setGroceryId(Integer groceryId) {
        this.groceryId = groceryId;
    }
    public String getGroceryName() {
        return groceryName;
    }
    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getInsertionId() { return insertionId; }
    public void setInsertionId(Integer insertionId) { this.insertionId = insertionId; }
    public String getUnit() { return this.unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public void insert() throws SQLException {
        insert("INSERT INTO \"Shopping list items\"" +
                " (shopping_list_id, grocery_id, amount) VALUES (?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setInt(1, SLId);
        s.setInt(2, groceryId);
        s.setBigDecimal(3, amount);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) {
    }

    @Override
    protected void updateFill(PreparedStatement s) {
    }

    // deletes all the items of the shopping list by id
    public void delete() throws SQLException {
        if (SLId == null) {
            throw new IllegalStateException("grocery SLId is not set");
        }

        delete("DELETE FROM \"Shopping list items\" WHERE shopping_list_id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, SLId);
    }
}