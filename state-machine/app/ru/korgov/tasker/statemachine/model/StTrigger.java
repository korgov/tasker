package ru.korgov.tasker.statemachine.model;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Fu;
import ru.korgov.util.func.Function;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:25
 */
public class StTrigger {
    private String fromState;
    private String trigger;
    private String toState;

    public StTrigger(final String fromState, final String trigger, final String toState) {
        this.fromState = fromState;
        this.trigger = trigger;
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getToState() {
        return toState;
    }

    public static final Function<JSONObject, StTrigger> FROM_JSON = new Fu<JSONObject, StTrigger>() {
        @Override
        public StTrigger apply(final JSONObject stTrigger) {
            try {
                return new StTrigger(
                        stTrigger.getString("fr"),
                        stTrigger.getString("trg"),
                        stTrigger.getString("to")
                );
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static final Function<StTrigger, JSONObject> TO_JSON = new Fu<StTrigger, JSONObject>() {
        @Override
        public JSONObject apply(final StTrigger stTrigger) {
            return JSONUtils.zip(
                    Cf.pair("fr", stTrigger.getFromState()),
                    Cf.pair("trg", stTrigger.getTrigger()),
                    Cf.pair("to", stTrigger.getToState())
            );
        }
    };

    @Override
    public String toString() {
        return "StTrigger{" +
                "fromState='" + fromState + '\'' +
                ", trigger='" + trigger + '\'' +
                ", toState='" + toState + '\'' +
                "}\n";
    }
}
