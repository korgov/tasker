package ru.korgov.tasker.statemachine.model;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Fu;
import ru.korgov.util.func.Function;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:30
 */
public class StTask {
    private final long id;
    private final String title;
    private final String description;
    private final StTaskType type;
    private final StateMachineConfiguration configuration;

    public StTask(final long id, final String title, final String description, final StTaskType type, final StateMachineConfiguration configuration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.configuration = configuration;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public StTaskType getType() {
        return type;
    }

    public StateMachineConfiguration getConfiguration() {
        return configuration;
    }

    public JSONObject asJson() {
        return JSONUtils.zip(
                Cf.pair("id", id),
                Cf.pair("title", title),
                Cf.pair("info", JSONUtils.zip(
                        Cf.pair("descr", description),
                        Cf.pair("type", type.name()),
                        Cf.pair("config", configuration.asJson())
                ))
        );
    }

    public static StTask fromJson(final JSONObject json) {
        try {
            final JSONObject info = json.getJSONObject("info");
            return new StTask(
                    json.getLong("id"),
                    json.getString("title"),
                    info.getString("descr"),
                    StTaskType.valueOf(info.getString("type")),
                    StateMachineConfiguration.fromJson(info.getJSONObject("config"))
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Function<JSONObject, StTask> FROM_JSON = new Fu<JSONObject, StTask>() {
        @Override
        public StTask apply(final JSONObject stTaskAsJson) {
            return fromJson(stTaskAsJson);
        }
    };

    public static final Function<StTask, JSONObject> TO_JSON = new Fu<StTask, JSONObject>() {
        @Override
        public JSONObject apply(final StTask stTask) {
            return stTask.asJson();
        }
    };

    @Override
    public String toString() {
        return "StTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", configuration=" + configuration +
                '}';
    }
}
