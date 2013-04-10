package ru.korgov.tasker.core.tasks;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 09.04.13 5:45
 */
public class TaskType {
    public static final RowMapper<TaskType> MAPPER = new RowMapper<TaskType>() {
        @Override
        public TaskType mapRow(ResultSet rs, int i) throws SQLException {
            return new TaskType(
                    rs.getLong("id"),
                    rs.getString("view_bean"),
                    rs.getString("solve_bean"),
                    rs.getString("description")
            );
        }
    };

    private long typeId;
    private String viewBean;
    private String solveBean;
    private String desc;

    public TaskType(long typeId, String viewBean, String solveBean, String desc) {
        this.typeId = typeId;
        this.viewBean = viewBean;
        this.solveBean = solveBean;
        this.desc = desc;
    }

    public long getTypeId() {
        return typeId;
    }

    public String getViewBean() {
        return viewBean;
    }

    public String getSolveBean() {
        return solveBean;
    }

    public String getDesc() {
        return desc;
    }
}
