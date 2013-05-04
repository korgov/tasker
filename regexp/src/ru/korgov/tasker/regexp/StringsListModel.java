package ru.korgov.tasker.regexp;

import javax.swing.*;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
@SuppressWarnings("SerializableHasSerializationMethods")
public class StringsListModel extends DefaultListModel {
    public StringsListModel(final List<String> values) {
        for (final String value : values) {
            addElement(value);
        }
    }
}
