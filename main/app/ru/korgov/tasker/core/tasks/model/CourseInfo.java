package ru.korgov.tasker.core.tasks.model;

import org.springframework.jdbc.core.RowMapper;
import ru.korgov.util.func.Function;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (korgov@yandex-team.ru)
 * Date: 13.05.13 0:31
 */
public class CourseInfo {

    public static final RowMapper<CourseInfo> MAPPER = new RowMapper<CourseInfo>() {
        @Override
        public CourseInfo mapRow(ResultSet rs, int i) throws SQLException {
            return new CourseInfo(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description")
            );
        }
    };

    private final long id;
    private final String title;
    private final String desc;

    public CourseInfo(long id, final String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static final Function<CourseInfo, Long> TO_ID = new Function<CourseInfo, Long>() {
        @Override
        public Long apply(final CourseInfo arg) {
            return arg.getId();
        }
    };
}
