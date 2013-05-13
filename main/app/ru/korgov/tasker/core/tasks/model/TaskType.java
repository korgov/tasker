package ru.korgov.tasker.core.tasks.model;

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
                    rs.getString("view_info"),
                    rs.getString("solve_info"),
                    rs.getString("description")
            );
        }
    };

    private long typeId;
    private String viewInfoAsJson;
    private String solveInfoAsJson;
    private String desc;

    public TaskType(long typeId, String viewInfoAsJson, String solveInfoAsJson, String desc) {
        this.typeId = typeId;
        this.viewInfoAsJson = viewInfoAsJson;
        this.solveInfoAsJson = solveInfoAsJson;
        this.desc = desc;
    }

    public long getTypeId() {
        return typeId;
    }

    public String getViewInfoAsJson() {
        return viewInfoAsJson;
    }

    public String getSolveInfoAsJson() {
        return solveInfoAsJson;
    }

    public String getDesc() {
        return desc;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TaskType taskType = (TaskType) o;

        if (typeId != taskType.typeId) return false;
        if (desc != null ? !desc.equals(taskType.desc) : taskType.desc != null) return false;
        if (solveInfoAsJson != null ? !solveInfoAsJson.equals(taskType.solveInfoAsJson) : taskType.solveInfoAsJson != null)
            return false;
        if (viewInfoAsJson != null ? !viewInfoAsJson.equals(taskType.viewInfoAsJson) : taskType.viewInfoAsJson != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (typeId ^ (typeId >>> 32));
        result = 31 * result + (viewInfoAsJson != null ? viewInfoAsJson.hashCode() : 0);
        result = 31 * result + (solveInfoAsJson != null ? solveInfoAsJson.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
