package ru.korgov.tasker.statemachine.viewers;

import org.json.JSONObject;
import ru.korgov.tasker.statemachine.model.StTask;
import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;
import ru.korgov.util.UrlUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Su;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 4:53
 */
public class StateMachineInputClient {
    private final JFrame frame;
    private final String hostname;
    private StTask task;
    private JPanel mainPanel;
    private JTable trigTable;
    private JTextField initStatesTextField;
    private JTextField finiteStatesTextField;
    private JButton checkButton;
    private JButton redrawButton;
    private JPanel titlePanel;
    private JLabel descriptionLabel;
    private JButton addTrigLineButton;
    private JButton removeTrigLineButton;
    private JPanel imagePanel;

    private final TriggerTableModel dataModel;

    private static final String JSON_CONTENT_TYPE = "application/json";

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public StateMachineInputClient(final JFrame frame, final String hostname, final StTask task) {
        this.frame = frame;
        this.hostname = hostname;
        this.task = task;


        final StateMachineConfiguration configuration = task.getConfiguration();
        initStatesTextField.setText(configuration.getInitialState());
        finiteStatesTextField.setText(Su.join(configuration.getFiniteStates(), ", "));
        dataModel = buildModel(configuration.getStTriggers());
        trigTable.setModel(dataModel);
        titlePanel.setBorder(BorderFactory.createTitledBorder(task.getTitle()));
        descriptionLabel.setText(task.getDescription());

        imagePanel.setLayout(new FlowLayout());

        addTrigLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addTrigLineAction();
            }
        });

        removeTrigLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                removeSelectedLineAction();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                checkAction();
            }
        });

        redrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                redrawImageAction();
            }
        });
    }

    private void removeSelectedLineAction() {
        final int selectedRow = trigTable.getSelectedRow();
        if (selectedRow != -1) {
            dataModel.removeRow(selectedRow);
        }
    }

    private void addTrigLineAction() {
        dataModel.addRow(StTrigger.EMPTY);
    }

    private void redrawImageAction() {
        final List<StTrigger> trigs = dataModel.getLines();
        final String initialState = initStatesTextField.getText();
        final Set<String> finiteStates = Cf.set(finiteStatesTextField.getText().split("[,\\s]+"));
        final StateMachineConfiguration configuration = new StateMachineConfiguration(initialState, trigs, finiteStates);
        if (!configuration.isValid()) {
            JOptionPane.showMessageDialog(frame, "Конфигурация некорректна", "Ошибка при построении конечного автомата!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            final JSONObject json = new JSONObject();
            json.put("task-id", task.getId());
            json.put("config", configuration.asJson());
            final byte[] testResultAsBytes = UrlUtils.sendPostRequestForBytes("http://" + hostname + "/binHelp", json.toString(), JSON_CONTENT_TYPE);
            final InputStream in = new ByteArrayInputStream(testResultAsBytes);
            final BufferedImage bImageFromConvert = ImageIO.read(in);
            final JLabel image = new JLabel(new ImageIcon(bImageFromConvert));
            imagePanel.removeAll();
            imagePanel.add(image);
            imagePanel.updateUI();
            frame.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при отправке на сервер!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkAction() {

    }

    private TriggerTableModel buildModel(final Collection<StTrigger> stTriggers) {
        return new TriggerTableModel(stTriggers, Cf.list("from-State", "Trigger", "to-State"));
    }
}
