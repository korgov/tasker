package ru.korgov.tasker.core.tasks.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.korgov.util.func.Function;

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
    private String title;

    public static final Function<Task, Long> TO_ID = new Function<Task, Long>() {
        @Override
        public Long apply(final Task arg) {
            return arg.getId();
        }
    };

    public JSONObject asJson() throws JSONException {
        final JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("type-id", taskTypeId);
        out.put("course-id", courseId);
        out.put("info", new JSONObject(jsonMetaInfo));
        out.put("title", title);
        return out;
    }

    public static Task fromJson(JSONObject task) throws JSONException {
        return new Task(
                task.getLong("id"),
                task.getLong("type-id"),
                task.getLong("course-id"),
                task.getString("info"),
                task.getString("title")
        );
    }

    public Task(long id, long taskType, long courseId, String jsonMetaInfo, final String title) {
        this.id = id;
        this.taskTypeId = taskType;
        this.courseId = courseId;
        this.jsonMetaInfo = jsonMetaInfo;
        this.title = title;
    }

    public String getTitle() {
        return title;
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
                    rs.getString("info"),
                    rs.getString("title")
            );
        }
    };

    public static final Function<Task, JSONObject> TO_JSON = new Function<Task, JSONObject>() {
        @Override
        public JSONObject apply(final Task arg) {
            try {
                return arg.asJson();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
}
