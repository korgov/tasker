package ru.korgov.tasker.modules.statemachine;

import com.googlecode.stateless4j.StateMachine;
import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;

import java.util.Set;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:18
 */
public class TaskerStateMachine extends StateMachine<String, String> {
    private final StateMachineConfiguration configuration;

    public TaskerStateMachine(final StateMachineConfiguration config) throws Exception {
        super(config.getInitialState());
        this.configuration = config;

        for (final StTrigger stTrigger : config.getStTriggers()) {
            this.Configure(stTrigger.getFromState())
                    .Permit(stTrigger.getTrigger(), stTrigger.getToState());
        }
    }

    public static boolean fire(final StateMachineConfiguration config, final String chars) throws Exception {
        final char[] arr = chars.toCharArray();
        final TaskerStateMachine machine = new TaskerStateMachine(config);
        final Set<String> finiteStates = machine.getFiniteStates();
        int i;
        for (i = 0; i < arr.length; i++) {
            if (finiteStates.contains(machine.getState())) {
                break;
            }
            final char c = arr[i];
            machine.Fire(String.valueOf(c));
        }
        return i == arr.length && finiteStates.contains(machine.getState());
    }

    private Set<String> getFiniteStates() {
        return configuration.getFiniteStates();
    }
}
