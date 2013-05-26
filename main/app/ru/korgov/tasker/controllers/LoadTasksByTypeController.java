package ru.korgov.tasker.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import play.api.libs.MimeTypes;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.util.Parsers;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import views.html.xerror;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 5:20
 */
public class LoadTasksByTypeController extends Controller implements SpringController {
    private static final String JSON_CONTENT_TYPE = MimeTypes.forExtension("json").get();

    @Autowired
    private TasksService tasksService;


    @Override
    @BodyParser.Of(BodyParser.Json.class)
    public Result process() {
        final Http.Request request = request();
        try {
            new JSONObject();
            final long taskTypeId = Parsers.parseLong(request.getQueryString("task-type-id"), -1L);
            final long courseId = Parsers.parseLong(request.getQueryString("course-id"), -1L);
            final List<Task> tasks = tasksService.loadTasks(courseId, taskTypeId);
            final JSONObject responseAsJson = new JSONObject(Cu.<String, Object>zipMap(
                    Cf.<String, Object>pair("tasks", new JSONArray(Task.TO_JSON.map(tasks)))
            ));
            return ok(responseAsJson.toString().getBytes(Charset.forName("UTF-8"))).as(JSON_CONTENT_TYPE);

        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }
    }
}
