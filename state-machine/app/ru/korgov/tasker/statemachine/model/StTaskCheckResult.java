package ru.korgov.tasker.statemachine.model;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cf;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 30.05.13 11:49
 */
public class StTaskCheckResult {
    public static final StTaskCheckResult OK = new StTaskCheckResult("", "", true);
    private final String failIncorrectStr;
    private final String failCorrectStr;
    private final boolean isOk;

    public StTaskCheckResult(final String failIncorrectStr, final String failCorrectStr, final boolean ok) {
        this.failIncorrectStr = failIncorrectStr;
        this.failCorrectStr = failCorrectStr;
        isOk = ok;
    }

    public String getFailIncorrectStr() {
        return failIncorrectStr;
    }

    public String getFailCorrectStr() {
        return failCorrectStr;
    }

    public boolean isOk() {
        return isOk;
    }

    public JSONObject asJson() {
        return JSONUtils.zip(
                Cf.pair("fail-correct-str", failCorrectStr),
                Cf.pair("fail-incorrect-str", failIncorrectStr),
                Cf.pair("is-ok", isOk)
        );
    }

    public static StTaskCheckResult fromJson(final JSONObject jsonObject) {
        try {
            return new StTaskCheckResult(
                    jsonObject.getString("fail-incorrect-str"),
                    jsonObject.getString("fail-correct-str"),
                    jsonObject.getBoolean("is-ok")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
