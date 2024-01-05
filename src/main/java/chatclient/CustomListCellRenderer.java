package chatclient;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

// TODO tämän voi poistaa jos ei käytä

public class CustomListCellRenderer extends JLabel implements ListCellRenderer<String> {

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
            boolean isSelected, boolean cellHasFocus) {
        setText(value);

        setOpaque(true);

        return this;
    }

}
