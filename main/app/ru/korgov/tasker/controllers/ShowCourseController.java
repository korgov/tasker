package ru.korgov.tasker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.*;
import ru.korgov.util.Parsers;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Fu;
import ru.korgov.util.alias.Fus;
import ru.korgov.util.collection.Option;
import ru.korgov.util.func.Function;
import views.html.course;
import views.html.xerror;

import java.util.List;
import java.util.Map;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 13.05.13 0:52
 */
public class ShowCourseController extends Controller implements SpringController {

    @Autowired
    private TasksService tasksService;

    @Override
    public Result process() {
        final Http.Request request = request();
        try {
            final List<CourseInfo> courseInfos = tasksService.loadAllCourses();
            final Map<Long, TaskType> taskTypes = tasksService.loadTypesInfo();
            final Map<Long, CourseInfo> idToCourse = asIdToCourse(courseInfos);
            final Long selectedCourseId = Parsers.parseLong(request.getQueryString("course-id"), -1L);
            final List<Task> tasks = tasksService.loadCourseTasks(selectedCourseId);
            final List<TaskCreatorInfo> taskCreatorInfos = tasksService.loadCourseCreators(selectedCourseId);
            final Fu<Task, TaskType> taskToTypeFu = taskToTypeFu(taskTypes);
            return ok(course.render(new Res(
                    courseInfos,
                    Option.avoidNull(idToCourse.get(selectedCourseId)),
                    taskCreatorInfos,
                    Cu.multiMapFromIterable(
                            taskToTypeFu,
                            new Function<Task, CompleteTaskInfo>() {
                                @Override
                                public CompleteTaskInfo apply(Task task) {
                                    return new CompleteTaskInfo(
                                            idToCourse.get(task.getCourseId()),
                                            taskToTypeFu.apply(task),
                                            task
                                    );
                                }
                            }, tasks
                    )
            )));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }

    }

    private Fu<Task, TaskType> taskToTypeFu(final Map<Long, TaskType> taskTypes) {
        return new Fu<Task, TaskType>() {
            @Override
            public TaskType apply(final Task task) {
                return taskTypes.get(task.getTaskTypeId());
            }
        };
    }

    private Map<Long, CourseInfo> asIdToCourse(List<CourseInfo> courseInfos) {
        return Cu.mapFromIterable(CourseInfo.TO_ID, Fus.<CourseInfo>id(), courseInfos);
    }

    public static class Res {
        private final String title;
        private final List<CourseInfo> allCourses;
        private final Option<CourseInfo> selectedCourse;
        private final Map<TaskType, List<CompleteTaskInfo>> taskInfos;
        private final List<TaskCreatorInfo> taskCreatorInfos;

        public Res(List<CourseInfo> allCourses, Option<CourseInfo> selectedCourse, final List<TaskCreatorInfo> taskCreatorInfos, Map<TaskType, List<CompleteTaskInfo>> taskInfos) {
            this.title = "Course" + (selectedCourse.hasValue() ? " - " + selectedCourse.getValue().getDesc() : "");
            this.allCourses = allCourses;
            this.taskCreatorInfos = taskCreatorInfos;
            this.selectedCourse = selectedCourse;
            this.taskInfos = taskInfos;
        }

        public Option<CourseInfo> getSelectedCourse() {
            return selectedCourse;
        }

        public List<TaskCreatorInfo> getTaskCreatorInfos() {
            return taskCreatorInfos;
        }

        public List<CourseInfo> getAllCourses() {
            return allCourses;
        }

        public Map<TaskType, List<CompleteTaskInfo>> getTaskInfos() {
            return taskInfos;
        }

        public String getTitle() {
            return title;
        }
    }
}
