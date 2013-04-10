package ru.korgov.util.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 10.04.13 8:05
 */
public class RowMappers {
    public static RowMapper<Long> longAt(final int index){
        return new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getLong(index);
            }
        };
    }

    public static RowMapper<Long> longAt(final String columnName){
        return new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getLong(columnName);
            }
        };
    }
}
