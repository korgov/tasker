package ru.korgov.tasker.core.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.korgov.tasker.core.users.model.UserInfo;
import ru.korgov.util.db.Setters;

import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 3:49 AM
 */
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(final UserInfo userInfo) {
        jdbcTemplate.update("insert into user(login, pwdhash, name) values(?,?,?)",
                Setters.chain(
                        Setters.stringAt(1, userInfo.getLogin()),
                        Setters.stringAt(2, userInfo.getPwdHash()),
                        Setters.stringAt(3, userInfo.getName())
                )
        );
    }

    public List<UserInfo> loadAllUsers() {
        return jdbcTemplate.query("select * from user", Setters.setFetchSize(1000), UserInfo.MAPPER);
    }

    public UserInfo loadById(final long userId) {
        return jdbcTemplate.queryForObject("select * from user where id = ?", UserInfo.MAPPER, userId);
    }
}
