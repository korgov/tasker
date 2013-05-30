package ru.korgov.tasker.statemachine.model;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Su;
import ru.korgov.util.func.Function;

import java.util.Collection;
import java.util.Set;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:24
 */
public class StateMachineConfiguration {
    public static final StateMachineConfiguration NOTHING = new StateMachineConfiguration("", Cf.<StTrigger>emptyL(), Cf.<String>emptyS());

    private final String initialState;
    private final Set<StTrigger> stTriggers;
    private final Set<String> finiteStates;

    public StateMachineConfiguration(final String initialState, final Collection<StTrigger> stTriggers, final Set<String> finiteStates) {
        this.initialState = initialState;
        this.stTriggers = Cf.newLinkedSet(stTriggers);
        this.finiteStates = finiteStates;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<StTrigger> getStTriggers() {
        return stTriggers;
    }

    public Set<String> getFiniteStates() {
        return finiteStates;
    }

    public boolean isValid() {
        //todo: more validation
        return !Su.isEmpty(initialState) && !Cu.isEmpty(finiteStates) && !Cu.isEmpty(stTriggers);
    }

    public static JSONObject toJson(final StateMachineConfiguration configuration) {
        return new JSONObject(Cu.<String, Object>zipMap(
                Cf.<String, Object>pair("init", configuration.getInitialState()),
                Cf.<String, Object>pair("trigs", new JSONArray(StTrigger.TO_JSON.map(configuration.getStTriggers()))),
                Cf.<String, Object>pair("finite", new JSONArray(configuration.getFiniteStates()))
        ));
    }

    public static StateMachineConfiguration fromJson(final JSONObject json) {
        return new StateMachineConfiguration(
                json.optString("init"),
                StTrigger.FROM_JSON.map(JSONUtils.asJSONObjectList(json.optJSONArray("trigs"))),
                Cf.newSet(JSONUtils.asStringList(json.optJSONArray("finite")))
        );
    }

    public static final Function<StateMachineConfiguration, JSONObject> TO_JSON = new Function<StateMachineConfiguration, JSONObject>() {
        @Override
        public JSONObject apply(final StateMachineConfiguration arg) {
            return toJson(arg);
        }
    };


    public static final Function<JSONObject, StateMachineConfiguration> FROM_JSON = new Function<JSONObject, StateMachineConfiguration>() {
        @Override
        public StateMachineConfiguration apply(final JSONObject arg) {
            return fromJson(arg);
        }
    };

    public JSONObject asJson() {
        return toJson(this);
    }

    @Override
    public String toString() {
        return "StateMachineConfiguration{" +
                "initialState='" + initialState + '\'' +
                ", finiteStates=" + finiteStates +
                ", stTriggers=" + stTriggers +
                '}';
    }
}
