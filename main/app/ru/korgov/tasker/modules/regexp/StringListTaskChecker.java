package ru.korgov.tasker.modules.regexp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Required;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.modules.TaskChecker;
import ru.korgov.tasker.regexp.model.RegExpTask;
import ru.korgov.tasker.regexp.model.RegExpTestResult;
import ru.korgov.tasker.regexp.serializer.RegExpConstants;
import ru.korgov.tasker.regexp.serializer.RegExpTaskSerialiser;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Fu;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 2:47
 */
public class StringListTaskChecker implements TaskChecker {

    private RegExpTaskSerialiser regExpTaskSerialiser;

    @Required
    public void setRegExpTaskSerialiser(final RegExpTaskSerialiser regExpTaskSerialiser) {
        this.regExpTaskSerialiser = regExpTaskSerialiser;
    }

    @Override
    public JSONObject checkTask(final Task rawTask, final JSONObject params) throws Exception {
        final JSONObject regExpTaskAsJson = new JSONObject(rawTask.getJsonMetaInfo());
        final RegExpTask regExpTask = regExpTaskSerialiser.jsonToTask(regExpTaskAsJson);
        final String regExp = params.getString(RegExpConstants.REGEXP_ATTR);
        final RegExpTestResult result = checkRegexp(regExpTask, regExp);
        return regExpTaskSerialiser.resultToJson(result);
    }

    private RegExpTestResult checkRegexp(final RegExpTask regExpTask, final String regExp) {
        final List<String> sourceData = regExpTask.getSourceData();
        final List<String> validResultData = regExpTask.getResultData();
        final List<String> resultsByMatch = Cf.newList();
        try {
            final Pattern pattern = Pattern.compile(regExp);
            for (final String src : sourceData) {
                resultsByMatch.addAll(matchStrings(src, pattern));
            }
        } catch (Exception ignored) {
        }

        final List<String> resultToShow = Cf.newList(resultsByMatch);
        int firstIncorrectIndex = resultToShow.size();
        final boolean isCorrect = Cf.newSet(validResultData).equals(Cf.newSet(resultsByMatch));
        if (!isCorrect) {
            final Set<String> invalidMissing = Cu.minus(validResultData, resultsByMatch);
            final Set<String> invalidIncorrect = Cu.minus(resultsByMatch, validResultData);
            resultToShow.removeAll(invalidIncorrect);
            firstIncorrectIndex = resultToShow.size();
            resultToShow.addAll(Cu.map(invalidMissing, prefixFu("[-] ")));
            resultToShow.addAll(Cu.map(invalidIncorrect, prefixFu("[+] ")));
        }
        return new RegExpTestResult(regExpTask.getId(), resultToShow, firstIncorrectIndex, isCorrect);
    }

    private Fu<String, String> prefixFu(final String prefix) {
        return new Fu<String, String>() {
            @Override
            public String apply(final String str) {
                return prefix + str;
            }
        };
    }

    private List<String> matchStrings(final String src, final Pattern pattern) {
        final List<String> out = Cf.newList();
        final Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            out.add(matcher.group());
        }
        return out;
    }
}
