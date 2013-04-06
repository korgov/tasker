package ru.korgov.tasker.core.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import play.db.DB;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 1:54 AM
 */
public class Database {

    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(DB.getDataSource());
    private static final DataSourceTransactionManager DATA_SOURCE_TRANSACTION_MANAGER = new DataSourceTransactionManager(DB.getDataSource());
    private static final TransactionTemplate TRANSACTION_TEMPLATE = new TransactionTemplate(DATA_SOURCE_TRANSACTION_MANAGER);

    private Database() {
    }

    public static JdbcTemplate jdbc() {
        return JDBC_TEMPLATE;
    }

    public static TransactionTemplate transactionTemplate() {
        return TRANSACTION_TEMPLATE;
    }
}
