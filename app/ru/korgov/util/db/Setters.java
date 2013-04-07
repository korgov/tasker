package ru.korgov.util.db;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 6:53 AM
 */
public class Setters {
    private Setters() {
    }

    public static PreparedStatementSetter chain(final PreparedStatementSetter... setters) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) throws SQLException {
                for (final PreparedStatementSetter setter : setters) {
                    setter.setValues(preparedStatement);
                }
            }
        };
    }

    public static PreparedStatementSetter longAt(final int index, final long value) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setLong(index, value);
            }
        };
    }

    public static PreparedStatementSetter stringAt(final int index, final String value) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(index, value);
            }
        };
    }

    public static PreparedStatementSetter setFetchSize(final int size) {
        return new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setFetchSize(size);
            }
        };
    }
}
