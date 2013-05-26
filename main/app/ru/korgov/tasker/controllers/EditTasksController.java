package ru.korgov.tasker.controllers;

import org.codehaus.jackson.JsonNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import play.api.libs.MimeTypes;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.func.Function;
import views.html.xerror;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 5:20
 */
public class EditTasksController extends Controller implements SpringController {
    private static final String JSON_CONTENT_TYPE = MimeTypes.forExtension("json").get();

    @Autowired
    private TasksService tasksService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    @BodyParser.Of(BodyParser.Json.class)
    public Result process() {
        final Http.Request request = request();
        try {
            new JSONObject();
            final JsonNode reqAsJson = request.body().asJson();
            System.out.println("text:" + reqAsJson.toString());
            final JSONObject req = new JSONObject(reqAsJson.toString());
            final JSONObject metaInfo = req.getJSONObject("meta-info");
            final long courseId = metaInfo.getLong("course-id");
            final long typeId = metaInfo.getLong("type-id");
            final List<JSONObject> tasksAsJson = JSONUtils.asJSONObjectList(req.getJSONArray("tasks"));
            final List<Task> tasks = Cu.map(new Function<JSONObject, Task>() {
                @Override
                public Task apply(final JSONObject task) {
                    try {
                        return new Task(
                                task.getLong("id"),
                                typeId,
                                courseId,
                                task.getJSONObject("info").toString(),
                                task.getString("title")
                        );
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, tasksAsJson);
            final List<Task> newTasksSet = transactionTemplate.execute(new TransactionCallback<List<Task>>() {
                @Override
                public List<Task> doInTransaction(final TransactionStatus transactionStatus) {
                    tasksService.saveTasks(tasks, courseId, typeId);
                    return tasksService.loadTasks(courseId, typeId);
                }
            });
            final JSONObject res = new JSONObject(Cu.<String, Object>zipMap(
                    Cf.<String, Object>pair("tasks", new JSONArray(Task.TO_JSON.map(newTasksSet)))
            ));
            return ok(res.toString().getBytes(Charset.forName("UTF-8"))).as(JSON_CONTENT_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }
    }
}
