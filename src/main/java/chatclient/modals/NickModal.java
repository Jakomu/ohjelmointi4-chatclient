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
        super(parent, new Dimension(500, 200), title, chatClient);
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(20));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(300, 40));
        titleLabel.setMaximumSize(new Dimension(300, 40));
        titleLabel.setMinimumSize(new Dimension(300, 40));
        titleLabel.setFont(TITLE_FONT);
        mainBox.add(titleLabel);
        mainBox.add(Box.createVerticalStrut(20));
        Box inputBox = Box.createHorizontalBox();
        JTextField nickField = new JTextField(NICKNAME_PLACEHOLDER);
        nickField.setPreferredSize(new Dimension(280, 40));
        nickField.setMaximumSize(new Dimension(280, 40));
        nickField.setMinimumSize(new Dimension(280, 40));
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
        inputBox.add(Box.createHorizontalStrut(20));
        JButton okButton = new JButton("OK");
        okButton.setFont(DEFAULT_FONT);
        okButton.setPreferredSize(new Dimension(40, 40));
        okButton.setMaximumSize(new Dimension(40, 40));
        okButton.setMinimumSize(new Dimension(40, 40));
        okButton.addActionListener(e -> {
            if (!nickField.getText().equals(NICKNAME_PLACEHOLDER) && !nickField.getText().equals("")) {
                chatClient.setNick(nickField.getText());
                dispose();
            }
        });

        inputBox.add(okButton);
        inputBox.add(Box.createHorizontalGlue());
        mainBox.add(inputBox);
        mainBox.add(Box.createVerticalStrut(40));

        add(mainBox);
    }

}
