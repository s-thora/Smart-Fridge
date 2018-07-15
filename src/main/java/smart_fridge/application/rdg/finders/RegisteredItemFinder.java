package smart_fridge.application.rdg.finders;

import smart_fridge.application.rdg.RegisteredItem;
import smart_fridge.application.rdg.RegisteredUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*
 *inspired by shanki
 */

public class RegisteredItemFinder extends BaseFinder<RegisteredItem> {

    private static final RegisteredItemFinder INSTANCE = new RegisteredItemFinder();

    public static RegisteredItemFinder getInstance() { return INSTANCE; }

    private RegisteredItemFinder() { }

    public RegisteredItem findInByInventoryId(int id) throws SQLException {
        return findByInt("select * from \"Registered items\" " +
                "where inventory_id = ? and type_id = 1", id);
    }



    @Override
    protected RegisteredItem load(ResultSet r) throws SQLException {
        RegisteredItem ri = new RegisteredItem();

        ri.setGroceryId(r.getInt("grocery_id"));
        ri.setInventoryId(r.getInt("inventory_id"));
        ri.setUsageId(r.getInt("usage_id"));
        ri.setAmount(r.getBigDecimal("amount"));
        ri.setTypeId(r.getInt("type_id"));

        return ri;
    }

}