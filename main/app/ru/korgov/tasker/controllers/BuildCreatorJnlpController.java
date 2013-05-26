package ru.korgov.tasker.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.controllers.jnlp.JnlpRes;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.TaskType;
import ru.korgov.util.Parsers;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import views.html.jnlpTemplate;
import views.html.xerror;

import java.util.List;
import java.util.Map;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 13.05.13 0:52
 */
public class BuildCreatorJnlpController extends Controller implements SpringController {

    @Autowired
    private TasksService tasksService;

    @Override
    public Result process() {
        final Http.Request request = request();
        try {
            final String hostname = request.host();
            final long taskTypeId = Parsers.parseLong(request.getQueryString("task-type-id"), -1L);
            final long courseId = Parsers.parseLong(request.getQueryString("course-id"), -1L);
            if (taskTypeId > 0L) {
                final Map<Long, TaskType> taskTypes = tasksService.loadTypesInfo();
                final TaskType taskType = taskTypes.get(taskTypeId);
                final JSONObject jnlpInfo = new JSONObject(taskType.getCreateInfoAsJson());
                final List<String> args = Cf.list(
                        new JSONObject(Cu.<String, Object>zipMap(
                                Cf.<String, Object>pair("hostname", hostname),
                                Cf.<String, Object>pair("tasks-data-query", "loadTasks?task-type-id=" + taskTypeId+"&course-id=" + courseId),
                                Cf.<String, Object>pair("meta-info", new JSONObject(Cu.<String, Object>zipMap(
                                        Cf.<String, Object>pair("course-id", courseId),
                                        Cf.<String, Object>pair("type-id", taskType.getTypeId())
                                )))
                        )).toString()
                );
                final String mainClass = jnlpInfo.getString("main-class");
                final String jarFilename = jnlpInfo.getString("jar-file");

                final JnlpRes res = new JnlpRes(hostname, mainClass, jarFilename, args, taskType.getDesc());
                return ok(jnlpTemplate.render(res)).as(JnlpRes.CONTENT_TYPE);
            } else {
                return ok(xerror.render("Incorrect task-type-id: " + taskTypeId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }

    }


}
