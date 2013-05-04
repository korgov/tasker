package ru.korgov.tasker.core.tasks;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 09.04.13 5:43
 */
public class Task {
    private long id;
    private long taskTypeId;
    private long courseId;
    private String jsonMetaInfo;

    public Task(long id, long taskType, long courseId, String jsonMetaInfo) {
        this.id = id;
        this.taskTypeId = taskType;
        this.courseId = courseId;
        this.jsonMetaInfo = jsonMetaInfo;
    }

    public long getTaskTypeId() {
        return taskTypeId;
    }

    public long getCourseId() {
        return courseId;
    }

    public long getId() {
        return id;
    }

    public String getJsonMetaInfo() {
        return jsonMetaInfo;
    }

    public static final ParameterizedRowMapper<Task> MAPPER = new ParameterizedRowMapper<Task>() {
        @Override
        public Task mapRow(ResultSet rs, int i) throws SQLException {
            return new Task(
                    rs.getLong("id"),
                    rs.getLong("type_id"),
                    rs.getLong("course_id"),
                    rs.getString("info")
            );
        }
    };
}
