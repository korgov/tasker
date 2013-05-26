package ru.korgov.tasker.core.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import ru.korgov.tasker.core.tasks.model.CourseInfo;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.core.tasks.model.TaskCreatorInfo;
import ru.korgov.tasker.core.tasks.model.TaskType;
import ru.korgov.util.Filter;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.collection.Pair;
import ru.korgov.util.db.DbUtil;
import ru.korgov.util.db.RowCallbacks;
import ru.korgov.util.db.RowMappers;
import ru.korgov.util.db.Setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
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
        return jdbcTemplate.query("select * from course", CourseInfo.MAPPER);
    }

    public Task loadTask(final long id) {
        return Cu.firstOrNull(loadTasks(Collections.singletonList(id)));
    }

    public List<Task> loadTasks(final Collection<Long> taskIds) {
        return DbUtil.queryWhereIn(jdbcTemplate, "select * from task where id in ", taskIds,
                Setters.setFetchSize(1000), Task.MAPPER);
    }

    public List<Task> loadAllTasks() {
        return jdbcTemplate.query("select * from task", Setters.setFetchSize(1000), Task.MAPPER);
    }

    public void createTasks(final Collection<Task> tasks) {
        jdbcTemplate.batchUpdate("insert into task(type_id, info, course_id, title) values(?, ?, ?, ?)", tasks, tasks.size(), new ParameterizedPreparedStatementSetter<Task>() {
            @Override
            public void setValues(PreparedStatement ps, Task task) throws SQLException {
                ps.setLong(1, task.getTaskTypeId());
                ps.setString(2, task.getJsonMetaInfo());
                ps.setLong(3, task.getCourseId());
                ps.setString(4, task.getTitle());
            }
        });
    }

    public Map<Long, TaskType> loadTypesInfo() {
        final Map<Long, TaskType> out = Cf.newMap();
        jdbcTemplate.query("select * from task_type",
                RowCallbacks.addToMap(out, RowMappers.longAt("id"), TaskType.MAPPER));
        return out;
    }

    public List<Task> loadCourseTasks(final long courseId) {
        if (courseId > 0L) {
            return jdbcTemplate.query("select * from task where course_id = ?",
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

    public List<Task> loadTasks(final long courseId, final long taskTypeId) {
        if (taskTypeId > 0L) {
            return jdbcTemplate.query("select * from task where type_id = ? and course_id = ?",
                    Setters.chain(
                            Setters.setFetchSize(1000),
                            Setters.longAt(1, taskTypeId),
                            Setters.longAt(2, courseId)
                    ),
                    Task.MAPPER
            );
        }
        return Cf.newList();
    }

    public void saveTasks(final Collection<Task> tasks, final long courseId, final long typeId) {
        final Pair<List<Task>, List<Task>> toInsertAndToUpdate = Cu.split(tasks, new Filter<Task>() {
            @Override
            public boolean fits(final Task x) {
                return x.getId() <= 0L;
            }
        });
        final Collection<Task> toInsert = toInsertAndToUpdate.getFirst();
        final Collection<Task> toUpdate = toInsertAndToUpdate.getSecond();
        final Collection<Long> toDelete = getToDelete(courseId, typeId, toUpdate);
        removeTasks(toDelete);
        updateTasks(toUpdate);
        createTasks(toInsert);
    }

    private Collection<Long> getToDelete(final long courseId, final long typeId, final Collection<Task> toUpdate) {
        final List<Long> oldTasks = Task.TO_ID.map(loadTasks(courseId, typeId));
        return Cu.minus(oldTasks, Task.TO_ID.map(toUpdate));
    }

    public void updateTasks(final Collection<Task> toUpdate) {
        jdbcTemplate.batchUpdate(
                "update task set type_id = ?, course_id = ?, info = ?, title = ? where id = ?",
                toUpdate, toUpdate.size(), new ParameterizedPreparedStatementSetter<Task>() {
            @Override
            public void setValues(final PreparedStatement ps, final Task task) throws SQLException {
                ps.setLong(1, task.getTaskTypeId());
                ps.setLong(2, task.getCourseId());
                ps.setString(3, task.getJsonMetaInfo());
                ps.setString(4, task.getTitle());
                ps.setLong(5, task.getId());
            }
        });
    }

    public void removeTasks(final Collection<Long> taskIds) {
        jdbcTemplate.batchUpdate("delete from task where id = ?", taskIds, taskIds.size(), new ParameterizedPreparedStatementSetter<Long>() {
            @Override
            public void setValues(final PreparedStatement ps, final Long taskId) throws SQLException {
                ps.setLong(1, taskId);
            }
        });
    }
}
