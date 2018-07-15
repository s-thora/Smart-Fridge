package smart_fridge.application.rdg.finders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import smart_fridge.application.DbContext;

/*
 *inspired by shanki
 */

public abstract class BaseFinder<T> {

    protected List<T> findAll(String query) throws SQLException {
        if (query == null) {
            throw new NullPointerException("query cannot be null");
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(query)) {
            try (ResultSet r = s.executeQuery()) {
                List<T> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;
            }
        }
    }

    T findByInt(String query, int value) throws SQLException {
        if (query == null) {
            throw new NullPointerException("query cannot be null");
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(query)) {
            s.setInt(1, value);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    T c = load(r);
                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return c;
                } else {
                    return null;
                }
            }
        }
    }

    List<T> findAllByInt(String query, int value) throws SQLException {
        if (query == null) {
            throw new NullPointerException("query cannot be null");
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(query)) {
            s.setInt(1, value);
            try (ResultSet r = s.executeQuery()) {
                List<T> elements = new ArrayList<>();
                while (r.next()) {
                    elements.add(load(r));
                }
                return elements;
            }
        }
    }

    protected abstract T load(ResultSet r) throws SQLException;
}
