package ru.korgov.util.db;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 10.04.13 6:29
 */
public class RowCallbacks {
    public static <T> RowCallbackHandler addToCollection(final Collection<T> c, final RowMapper<T> m) {
        return new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                c.add(m.mapRow(resultSet, 0));
            }
        };
    }

    public static <K, V> RowCallbackHandler addToMap(final Map<K, V> map, final RowMapper<K> keyMapper, final RowMapper<V> valueMapper) {
        return new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                map.put(keyMapper.mapRow(resultSet, 0), valueMapper.mapRow(resultSet, 0));
            }
        };
    }
}
