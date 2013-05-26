package ru.korgov.tasker.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.controllers.jnlp.JnlpRes;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.Task;
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
public class BuildTaskJnlpController extends Controller implements SpringController {

    @Autowired
    private TasksService tasksService;

    @Override
    public Result process() {
        final Http.Request request = request();
        try {
            final String hostname = request.host();
            final long taskId = Parsers.parseLong(request.getQueryString("task-id"), -1L);
            if (taskId > 0L) {
                final Task task = tasksService.loadTask(taskId);
                final Map<Long, TaskType> taskTypes = tasksService.loadTypesInfo();
                final TaskType taskType = taskTypes.get(task.getTaskTypeId());
                final JSONObject jnlpInfo = new JSONObject(taskType.getViewInfoAsJson());
                final List<String> args = Cf.list(
                        new JSONObject(Cu.<String, Object>zipMap(
                                Cf.pair("task-context", task.asJson()),
                                Cf.pair("task-type-context", taskType.asJson()),
                                Cf.pair("hostname", hostname)
                        )).toString()
                );
                final String mainClass = jnlpInfo.getString("main-class");
                final String jarFilename = jnlpInfo.getString("jar-file");

                final JnlpRes res = new JnlpRes(hostname, mainClass, jarFilename, args, task.getTitle());
                return ok(jnlpTemplate.render(res)).as(JnlpRes.CONTENT_TYPE);
            } else {
                return ok(xerror.render("Incorrect task-id: " + taskId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }

    }
}
