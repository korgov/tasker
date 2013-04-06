package ru.korgov.tasker.controllers;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.tasker.core.db.Database;
import views.html.index;
import views.html.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Application extends Controller {

    public static Result test() {

        final List<String> logins = Database.jdbc().query("SELECT login FROM user", new ParameterizedRowMapper<String>() {
            @Override
            public String mapRow(final ResultSet resultSet, final int i) throws SQLException {
                return resultSet.getString("login");
            }
        });

        return ok(test.render(logins));
    }


    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
