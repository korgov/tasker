package ru.korgov.tasker.core.tasks.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 13.05.13 0:42
 */
public class TaskCreatorInfo {
    private final long courseId;
    private final long taskTypeId;
    private final String desc;

    public long getCourseId() {
        return courseId;
    }

    public long getTaskTypeId() {
        return taskTypeId;
    }

    public String getDesc() {
        return desc;
    }

    public TaskCreatorInfo(final long courseId, final long taskTypeId, final String desc) {
        this.courseId = courseId;
        this.taskTypeId = taskTypeId;
        this.desc = desc;
    }

    public static final RowMapper<TaskCreatorInfo> MAPPER = new RowMapper<TaskCreatorInfo>() {
        @Override
        public TaskCreatorInfo mapRow(ResultSet rs, int i) throws SQLException {
            return new TaskCreatorInfo(
                    rs.getLong("course_id"),
                    rs.getLong("task_type_id"),
                    rs.getString("description")
            );
        }
    };
}
