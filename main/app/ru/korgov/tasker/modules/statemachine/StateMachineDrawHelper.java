package ru.korgov.tasker.modules.statemachine;

import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.util.graphviz.GraphViz;
import org.json.JSONObject;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.modules.BinaryTaskHelper;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;
import ru.korgov.util.graphviz.GraphVizHelper;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 30.05.13 3:40
 */
public class StateMachineDrawHelper implements BinaryTaskHelper {
    @Override
    public byte[] forTask(final Task rawTask, final JSONObject params) throws Exception {
        final JSONObject configAsJson = params.getJSONObject("config");
        final StateMachineConfiguration config = StateMachineConfiguration.fromJson(configAsJson);
        return new GraphViz().getGraph(convertAsDot(config), "gif");
    }

    private String convertAsDot(final StateMachineConfiguration config) {
        final GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        for (final StTrigger t : config.getStTriggers()) {
            GraphVizHelper.addLabeledEdge(gv, t.getFromState(), t.getToState(), t.getTrigger());
        }
        for (final String v : config.getFiniteStates()) {
            GraphVizHelper.addVertexPeripheries(gv, v, 2);
        }
        GraphVizHelper.addVertexColor(gv, config.getInitialState(), "brown4");
        gv.addln(gv.end_graph());
        return gv.getDotSource();
    }
}
