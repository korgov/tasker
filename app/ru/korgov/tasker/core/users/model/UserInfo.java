package ru.korgov.tasker.core.users.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.korgov.util.alias.Fu;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 3:50 AM
 */
public class UserInfo {
    public static final RowMapper<UserInfo> MAPPER = new ParameterizedRowMapper<UserInfo>() {
        @Override
        public UserInfo mapRow(final ResultSet rs, final int i) throws SQLException {
            return of(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("pwdhash"),
                    rs.getString("name")
            );
        }
    };
    private final long id;
    private final String login;
    private final String pwdHash;
    private final String name;

    private UserInfo(final long id, final String login, final String pwdHash, final String name) {
        this.id = id;
        this.login = login;
        this.pwdHash = pwdHash;
        this.name = name;
    }

    public static UserInfo of(final String login, final String pwdHash, final String name) {
        return of(-1L, login, pwdHash, name);
    }

    public static UserInfo of(final long id, final String login, final String pwdHash, final String name) {
        return new UserInfo(id, login, pwdHash, name);
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public String getName() {
        return name;
    }

    public static final Fu<UserInfo, String> TO_NAME = new Fu<UserInfo, String>() {
        @Override
        public String apply(final UserInfo info) {
            return info.getName();
        }
    };


}
