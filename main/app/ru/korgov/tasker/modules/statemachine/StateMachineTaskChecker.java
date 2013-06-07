package ru.korgov.tasker.modules.statemachine;

import org.json.JSONObject;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.modules.TaskChecker;
import ru.korgov.tasker.statemachine.model.StTask;
import ru.korgov.tasker.statemachine.model.StTaskCheckResult;
import ru.korgov.tasker.statemachine.model.StTaskType;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;

import java.util.List;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 2:47
 */
public class StateMachineTaskChecker implements TaskChecker {

    @Override
    public JSONObject checkTask(final Task rawTask, final JSONObject params) throws Exception {
        final StTask stTask = StTask.fromJson(rawTask.asJson());
        final StateMachineConfiguration config = StateMachineConfiguration.fromJson(params.getJSONObject("config"));
        final StTaskCheckResult result = checkConfig(stTask, config);
        return result.asJson();
    }

    private StTaskCheckResult checkConfig(final StTask stTask, final StateMachineConfiguration config) throws Exception {
        if (stTask.getType() == StTaskType.CREATE_ST_MACHINE) {
            final List<String> corrects = stTask.getCorrectStrings();
            final List<String> incorrects = stTask.getIncorrectStrings();
            for (final String correct : corrects) {
                if (!TaskerStateMachine.fire(config, correct)) {
                    return new StTaskCheckResult("", correct, false);
                }
            }

            for (final String incorrect : incorrects) {
                if (TaskerStateMachine.fire(config, incorrect)) {
                    return new StTaskCheckResult(incorrect, "", false);
                }
            }

            return StTaskCheckResult.OK;
        }
        throw new IllegalArgumentException(stTask.getType().name());
    }
}
