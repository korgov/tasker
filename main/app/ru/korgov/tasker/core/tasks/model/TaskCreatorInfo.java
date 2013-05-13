package ru.korgov.tasker.core.tasks.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 13.05.13 0:42
 */
public class TaskCreatorInfo {
    private final long id;
    private final long courseId;
    private final String metaInfoAsJson;
    private final String desc;

    public TaskCreatorInfo(long id, long courseId, String metaInfoAsJson, String desc) {
        this.id = id;
        this.courseId = courseId;
        this.metaInfoAsJson = metaInfoAsJson;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public long getCourseId() {
        return courseId;
    }

    public String getMetaInfoAsJson() {
        return metaInfoAsJson;
    }

    public String getDesc() {
        return desc;
    }

    public static final RowMapper<TaskCreatorInfo> MAPPER = new RowMapper<TaskCreatorInfo>() {
        @Override
        public TaskCreatorInfo mapRow(ResultSet rs, int i) throws SQLException {
            return new TaskCreatorInfo(
                    rs.getLong("id"),
                    rs.getLong("course_id"),
                    rs.getString("info"),
                    rs.getString("description")
            );
        }
    };
}
