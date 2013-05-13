package ru.korgov.tasker.core.tasks.model;

import ru.korgov.util.alias.Fu;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 13.05.13 1:13
 */
public class CompleteTaskInfo {
    private final CourseInfo courseInfo;
    private final TaskType taskType;
    private final Task task;

    public CompleteTaskInfo(CourseInfo courseInfo, TaskType taskType, Task task) {
        this.courseInfo = courseInfo;
        this.taskType = taskType;
        this.task = task;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Task getTask() {
        return task;
    }

    public static final Fu<CompleteTaskInfo, TaskType> TO_TASK_TYPE = new Fu<CompleteTaskInfo, TaskType>() {
        @Override
        public TaskType apply(final CompleteTaskInfo arg) {
            return arg.getTaskType();
        }
    };
}
