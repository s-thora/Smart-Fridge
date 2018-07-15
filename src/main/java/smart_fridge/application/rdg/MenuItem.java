package smart_fridge.application.rdg;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *inspired by shanki
 */

public class MenuItem extends BaseGateway {

    private Integer id;
    private String dishName;
    private String recipe;
    private String cookingTime;
    private BigDecimal price;

    public Integer getId() { return id;	}
    public void setId(Integer id) { this.id = id; }
    public String getDishName() {
        return dishName;
    }
    public void setDishName(String groceryName) {
        this.dishName = groceryName;
    }
    public String getRecipe() { return recipe; }
    public void setRecipe(String recipe) { this.recipe = recipe; }
    public String getCookingTime() { return cookingTime; }
    public void setCookingTime(String  cookingTime) { this.cookingTime = cookingTime; }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void insert() throws SQLException {
        if (id != null) {
            throw new IllegalStateException("id has been set");
        }

        insert("INSERT INTO \"Menu\" " +
                "(dish_name, price, recipe_content, cooking_time) VALUES (?,?,?,?)");
    }

    @Override
    protected void insertFill(PreparedStatement s) throws SQLException {
        s.setString(1, dishName);
        s.setBigDecimal(2, price);
        s.setString(3, recipe);
        s.setString(4, cookingTime);
    }

    @Override
    protected void insertUpdateKeys(ResultSet r) throws SQLException {
        id = r.getInt(1);
    }

    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        update("UPDATE \"Menu\" " +
                "SET dish_name = ?, price = ?, recipe_content = ?, cooking_time = ?" +
                "WHERE id = ?");
    }

    @Override
    protected void updateFill(PreparedStatement s) throws SQLException {
        s.setString(1, dishName);
        s.setBigDecimal(2, price);
        s.setString(3, recipe);
        s.setString(4, cookingTime);
        s.setInt(5, id);
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }

        delete("DELETE FROM \"Menu\" WHERE id = ?");
    }

    @Override
    protected void deleteFill(PreparedStatement s) throws SQLException {
        s.setInt(1, id);
    }

}