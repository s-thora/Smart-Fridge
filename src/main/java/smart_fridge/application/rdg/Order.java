package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Order extends BaseGateway {

    private Integer id;
    private BigDecimal sum;
    private Timestamp orderDate;
    private Integer shoppingListId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public BigDecimal getSum() {
        return sum;
    }
    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    public Integer getShoppingListId() {
        return shoppingListId;
    }
    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public void insert() throws SQLException {
        if (id != null) {
            throw new IllegalStateException("id has been set");
        }

        insert("INSERT INTO \"Orders\" " +
                "(sum, order_date, shopping_list_id) VALUES (?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setBigDecimal(1, sum);
        s.setTimestamp(2, orderDate);
        s.setInt(3, shoppingListId);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        id = r.getInt(1);
    }

    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        update("UPDATE \"Orders\" " +
                "SET sum = ?, order_date = ?, shopping_list_id = ?" +
                "WHERE id = ?");
    }

    @Override
    protected void updateFill(PreparedStatement s) throws SQLException {
        s.setBigDecimal(1, sum);
        s.setTimestamp(2, orderDate);
        s.setInt(3, shoppingListId);
        s.setInt(4, id);
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Orders\" WHERE id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
    }
}
