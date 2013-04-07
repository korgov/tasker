package ru.korgov.tasker.core.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import ru.korgov.tasker.core.users.model.UserInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 3:49 AM
 */
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(final UserInfo userInfo) {
        jdbcTemplate.update("insert into user(login, pwdhash, name) values(?,?,?)", new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, userInfo.getLogin());
                ps.setString(2, userInfo.getPwdHash());
                ps.setString(3, userInfo.getName());
            }
        });
    }

    public List<UserInfo> loadAllUsers() {
        return jdbcTemplate.query("select * from user", UserInfo.MAPPER);
    }

    public UserInfo loadById(final long userId) {
        return jdbcTemplate.queryForObject("select * from user where id = ?", UserInfo.MAPPER, userId);
    }
}
