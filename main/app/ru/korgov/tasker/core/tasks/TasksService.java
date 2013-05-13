package ru.korgov.tasker.core.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import ru.korgov.tasker.core.tasks.model.CourseInfo;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.core.tasks.model.TaskCreatorInfo;
import ru.korgov.tasker.core.tasks.model.TaskType;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.db.DbUtil;
import ru.korgov.util.db.RowCallbacks;
import ru.korgov.util.db.RowMappers;
import ru.korgov.util.db.Setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 09.04.13 5:59
 */
public class TasksService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CourseInfo> loadAllCourses() {
        return jdbcTemplate.query("SELECT * FROM course", CourseInfo.MAPPER);
    }

    public Task loadTask(final long id) {
        return Cu.firstOrNull(loadTasks(Collections.singletonList(id)));
    }

    public List<Task> loadTasks(final List<Long> taskIds) {
        return DbUtil.queryWhereIn(jdbcTemplate, "SELECT * FROM task WHERE id IN ", taskIds,
                Setters.setFetchSize(1000), Task.MAPPER);
    }

    public List<Task> loadAllTasks() {
        return jdbcTemplate.query("SELECT * FROM task", Setters.setFetchSize(1000), Task.MAPPER);
    }

    public void createTasks(final List<Task> tasks) {
        jdbcTemplate.batchUpdate("INSERT INTO task(type_id, info, course_id) VALUES(?, ?, ?)", tasks, tasks.size(), new ParameterizedPreparedStatementSetter<Task>() {
            @Override
            public void setValues(PreparedStatement ps, Task task) throws SQLException {
                ps.setLong(1, task.getTaskTypeId());
                ps.setString(2, task.getJsonMetaInfo());
                ps.setLong(3, task.getCourseId());
            }
        });
    }

    public Map<Long, TaskType> loadTypesInfo() {
        final Map<Long, TaskType> out = Cf.newMap();
        jdbcTemplate.query("SELECT * FROM task_type",
                RowCallbacks.addToMap(out, RowMappers.longAt("id"), TaskType.MAPPER));
        return out;
    }

    public List<Task> loadCourseTasks(final long courseId) {
        if (courseId > 0L) {
            return jdbcTemplate.query("SELECT * FROM task WHERE course_id = ?",
                    Setters.chain(
                            Setters.setFetchSize(1000),
                            Setters.longAt(1, courseId)
                    ),
                    Task.MAPPER
            );
        }
        return Cf.newList();
    }

    public List<TaskCreatorInfo> loadCourseCreators(final Long courseId) {
        return jdbcTemplate.query("select * from task_creator where course_id = ?",
                Setters.chain(
                        Setters.setFetchSize(100),
                        Setters.longAt(1, courseId)
                ),
                TaskCreatorInfo.MAPPER
        );
    }
}
