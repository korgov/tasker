package ru.korgov.tasker.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.Spring;
import ru.korgov.tasker.core.users.UserService;
import ru.korgov.tasker.core.users.model.UserInfo;
import views.html.index;
import views.html.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Application extends Controller {

    public static Result test() {

        final JdbcTemplate jdbcTemplate = Spring.getBeanOfType(JdbcTemplate.class);
        final TransactionTemplate transactionTemplate = Spring.getBeanOfType(TransactionTemplate.class);

        final List<String> logins = transactionTemplate.execute(new TransactionCallback<List<String>>() {
            @Override
            public List<String> doInTransaction(final TransactionStatus transactionStatus) {
                return jdbcTemplate.query("SELECT login FROM user", new ParameterizedRowMapper<String>() {
                    @Override
                    public String mapRow(final ResultSet resultSet, final int i) throws SQLException {
                        return resultSet.getString("login");
                    }
                });
            }
        });
        return ok(test.render(logins));

    }


    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result test2() {
        final UserService userService = Spring.getBeanOfType(UserService.class);
        return ok(test.render(UserInfo.TO_NAME.map(userService.loadAllUsers())));
    }

    public static Result test3(final String msg) {
        return ok(index.render(msg));
    }


}
