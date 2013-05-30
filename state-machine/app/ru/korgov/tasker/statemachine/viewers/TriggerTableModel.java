package ru.korgov.tasker.statemachine.viewers;

import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.util.alias.Fu;

import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 29.05.13 21:01
 */
public class TriggerTableModel extends DefaultTableModel {

    public static final Fu<Object, StTrigger> RAW_LINE_TO_TRIG = new Fu<Object, StTrigger>() {
        @Override
        public StTrigger apply(final Object rowObj) {
            final Vector row = (Vector) rowObj;
            final String fromState = String.valueOf(row.get(0));
            final String trigger = String.valueOf(row.get(1));
            final String toState = String.valueOf(row.get(2));
            return new StTrigger(fromState, trigger, toState);
        }
    };

    public TriggerTableModel(final Collection<StTrigger> lines, final List<String> columnNames) {
        addColumns(columnNames);
        addRows(lines);
    }

    private void addRows(final Collection<StTrigger> lines) {
        for (final StTrigger line : lines) {
            addRow(line);
        }
    }

    public void addRow(final StTrigger line) {
        super.addRow(new Object[]{line.getFromState(), line.getTrigger(), line.getToState()});
    }

    private void addColumns(final List<String> columnNames) {
        for (final String columnName : columnNames) {
            super.addColumn(columnName);
        }
    }

    public List<StTrigger> getLines() {
        return RAW_LINE_TO_TRIG.map(dataVector);
    }
}
