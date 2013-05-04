package ru.korgov.util.db;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import ru.korgov.util.alias.*;

import java.util.Collection;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 10.04.13 5:50
 */
public class DbUtil {

    public static final Fu<Object, String> QUOTE_FU = new Fu<Object, String>() {
        @Override
        public String apply(Object str) {
            return "'" + str + "'";
        }
    };

    public static <T> List<T> queryWhereIn(final JdbcOperations jdbc, final String sql, final Collection<? extends Number> ins,
                                           final PreparedStatementSetter pss, final RowMapper<T> rm) {
        return queryWhereInRaw(jdbc, sql, ins, pss, rm, Fus.stringValueOf());
    }

    public static void queryWhereIn(final JdbcOperations jdbc, final String sql, final Collection<? extends Number> ins,
                                    final PreparedStatementSetter pss, final RowCallbackHandler rch) {
        queryWhereInRaw(jdbc, sql, ins, pss, rch, Fus.stringValueOf());
    }

    public static <T> List<T> queryWhereInRaw(final JdbcOperations jdbc, final String sql, final Collection<?> ins,
                                              final PreparedStatementSetter pss, final RowMapper<T> rm, final Fu<Object, String> wrapFu) {
        final List<T> out = Cf.newList();
        queryWhereInRaw(jdbc, sql, ins, pss, RowCallbacks.addToCollection(out, rm), wrapFu);
        return out;
    }

    public static void queryWhereInRaw(final JdbcOperations jdbc, final String sql, final Collection<?> ins,
                                       final PreparedStatementSetter pss, final RowCallbackHandler rch, final Fu<Object, String> wrapFu) {
        for (final List<?> chunk : Cu.split(ins, 1000)) {
            jdbc.query(sql + " " + getInSection(chunk, wrapFu), pss, rch);
        }
    }

    public static String getInSection(final Collection<? extends Number> nums) {
        return getInSection(nums, Fus.stringValueOf());
    }

    public static String getInSectionWithQuotes(final Collection<String> strs) {
        return getInSection(strs, QUOTE_FU);
    }

    public static String getInSection(final Collection<?> ins, final Fu<Object, String> wrapFu) {
        return " (" + Su.join(wrapFu.map(ins), ",") + ") ";
    }
}
