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

    public static final StTrigger EMPTY = new StTrigger("", "", "");

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

    public void setFromState(final String fromState) {
        this.fromState = fromState;
    }

    public void setTrigger(final String trigger) {
        this.trigger = trigger;
    }

    public void setToState(final String toState) {
        this.toState = toState;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StTrigger stTrigger = (StTrigger) o;

        if (fromState != null ? !fromState.equals(stTrigger.fromState) : stTrigger.fromState != null) return false;
        if (toState != null ? !toState.equals(stTrigger.toState) : stTrigger.toState != null) return false;
        if (trigger != null ? !trigger.equals(stTrigger.trigger) : stTrigger.trigger != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromState != null ? fromState.hashCode() : 0;
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        result = 31 * result + (toState != null ? toState.hashCode() : 0);
        return result;
    }
}
