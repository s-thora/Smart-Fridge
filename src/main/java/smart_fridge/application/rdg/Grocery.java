package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *inspired by shanki
 */

public class Grocery extends BaseGateway {

    Integer groceryId;
    private String groceryName;
    private BigDecimal price;
    private BigDecimal minimalAmount;
    private Integer keepingTime;
    private Integer categoryId;
    private String categoryName;
    private String unit;

    public Integer getId() { return groceryId;	}
    public void setId(Integer id) { this.groceryId = id; }
    public String getGroceryName() {
        return groceryName;
    }
    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getMinimalAmount() {
        return minimalAmount;
    }
    public void setMinimalAmount(BigDecimal minimalAmount) {
        this.minimalAmount = minimalAmount;
    }
    public Integer getKeepingTime() {
        return keepingTime;
    }
    public void setKeepingTime(Integer keepingTime) {
        this.keepingTime = keepingTime;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getUnit() { return this.unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public void insert() throws SQLException {
        if (groceryId != null) {
            throw new IllegalStateException("id has been set");
        }

        insert("INSERT INTO \"Groceries\" " +
                "(grocery_name, price, minimal_amount, keeping_time, category_id) VALUES (?,?,?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setString(1, groceryName);
        s.setBigDecimal(2, price);
        s.setBigDecimal(3, minimalAmount);
        s.setInt(4, keepingTime);
        s.setInt(5, categoryId);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        groceryId = r.getInt(1);
    }

    public void update() throws SQLException {
        if (groceryId == null) {
            throw new IllegalStateException("id is not set");
        }

        update("UPDATE \"Groceries\" " +
                "SET grocery_name = ?, minimal_amount = ?, keeping_time = ?, category_id = ?, price = ?" +
                "WHERE id = ?");
    }

    @Override
    protected void updateFill(PreparedStatement s) throws SQLException {
        s.setString(1, groceryName);
        s.setBigDecimal(2, minimalAmount);
        s.setInt(3, keepingTime);
        s.setInt(4, categoryId);
        s.setBigDecimal(5, price);
        s.setInt(6, groceryId);
    }

    public void delete() throws SQLException {
        if (groceryId == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Groceries\" WHERE id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, groceryId);
    }


}