package ru.korgov.tasker.modules.statemachine;

import ru.korgov.util.graphviz.GraphViz;
import org.junit.Test;
import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.graphviz.GraphVizHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 30.05.13 3:57
 */
public class StateMachineDrawHelperTest {
    @Test
    public void testDraw() throws Exception {
        final StateMachineConfiguration config = new StateMachineConfiguration("A", Cf.list(
                new StTrigger("A", "x", "B"),
                new StTrigger("A", "m", "B"),
                new StTrigger("B", "z", "B"),
                new StTrigger("A", "y", "C"),
                new StTrigger("B", "h", "C"),
                new StTrigger("B", "k", "D"),
                new StTrigger("B", "p", "E")
        ), Cf.set("C", "E"));

        final String dotSrc = convertAsDot(config);
        final byte[] graph = new GraphViz().getGraph(dotSrc, "gif");

        final InputStream in = new ByteArrayInputStream(graph);
        final BufferedImage bImageFromConvert = ImageIO.read(in);

        final JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(bImageFromConvert)));
        frame.pack();
        frame.setVisible(true);

        Thread.sleep(System.currentTimeMillis());
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
