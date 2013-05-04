package ru.korgov.tasker.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import play.api.libs.MimeTypes;
import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.Spring;
import ru.korgov.util.alias.Cf;
import views.html.index;
import views.html.index2;
import views.html.mainmenu;
import views.xml.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Application extends Controller {

    public static final String JNLP_CONTENT_TYPE = MimeTypes.forExtension("jnlp").get();

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
        return ok(mainmenu.render(logins));

    }


    public static Result index() {

        return ok(index.render("Your new application is ready."));
    }

    public static Result index2() {
        return ok(index2.render("Your new application is ready.", Cf.list("hello66", "hello77")));
    }

    public static Result test2() {
        final String nameValue = request().getQueryString("name");
        if (nameValue != null) {
            System.out.println(nameValue);
            return ok(test.render(nameValue)).as("application/x-java-jnlp-file");
        }
        System.out.println("Nothing");
        return ok(test.render("Main")).as(JNLP_CONTENT_TYPE);
    }

    public static Result test3(final String msg) {
        return ok(index.render(msg));
    }


}
