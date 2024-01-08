package chatclient.modals;

import static chatclient.resources.constants.*;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import chatclient.ChatClient;

public class NickModal extends Modal {

    public NickModal(JFrame parent, String title, ChatClient chatClient) {
        super(parent, new Dimension(getMyUnit(25), getMyUnit(10)), title, chatClient);
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        titleLabel.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        titleLabel.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        titleLabel.setFont(TITLE_FONT);
        mainBox.add(titleLabel);
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        Box inputBox = Box.createHorizontalBox();
        JTextField nickField = new JTextField(NICKNAME_PLACEHOLDER);
        nickField.setPreferredSize(new Dimension(getMyUnit(14), getMyUnit(2)));
        nickField.setMaximumSize(new Dimension(getMyUnit(14), getMyUnit(2)));
        nickField.setMinimumSize(new Dimension(getMyUnit(14), getMyUnit(2)));
        nickField.setFont(DEFAULT_FONT);
        nickField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (nickField.getText().equals(NICKNAME_PLACEHOLDER)) {
                    nickField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (nickField.getText().equals("")) {
                    nickField.setText(NICKNAME_PLACEHOLDER);
                }
            }
        });
        inputBox.add(Box.createHorizontalGlue());
        inputBox.add(nickField);
        inputBox.add(Box.createHorizontalStrut(getMyUnit(1)));
        JButton okButton = new JButton("OK");
        okButton.setFont(DEFAULT_FONT);
        okButton.setPreferredSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        okButton.setMaximumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        okButton.setMinimumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        okButton.addActionListener(e -> {
            if (!nickField.getText().equals(NICKNAME_PLACEHOLDER) && !nickField.getText().equals("")) {
                chatClient.setNick(nickField.getText());
                dispose();
            }
        });
        inputBox.add(okButton);
        inputBox.add(Box.createHorizontalGlue());
        mainBox.add(inputBox);
        mainBox.add(Box.createVerticalStrut(getMyUnit(2)));

        add(mainBox);
    }

}
