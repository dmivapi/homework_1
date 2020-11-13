package com.epam.dmivapi.repository.impl.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityMapper<T> {
    public abstract T mapRow(ResultSet rs) throws SQLException;

    public List<T> mapRows(ResultSet rs) {
        List<T> items = new ArrayList<T>();
        try {
            while (rs.next())
                items.add(mapRow(rs));
            return items;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
