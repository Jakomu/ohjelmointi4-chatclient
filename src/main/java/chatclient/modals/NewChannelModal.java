package chatclient.modals;

import static chatclient.resources.constants.*;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import chatclient.ChatClient;

public class NewChannelModal extends Modal {

    public NewChannelModal(JFrame parent, String title, ChatClient chatClient) {
        super(parent, new Dimension(getMyUnit(20), getMyUnit(14)), title, chatClient);

        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(getMyUnit(20), getMyUnit(2)));
        topBox.setMaximumSize(new Dimension(getMyUnit(20), getMyUnit(2)));
        topBox.setMinimumSize(new Dimension(getMyUnit(20), getMyUnit(2)));
        topBox.add(Box.createHorizontalStrut(getMyUnit(3)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        topBox.add(Box.createHorizontalGlue());
        topBox.add(titleLabel);
        topBox.add(Box.createHorizontalGlue());
        URL imgUrl = getClass().getResource("../resources/icons/close-30.png");
        ImageIcon closeImageIcon = new ImageIcon(imgUrl);
        JLabel closeButton = new JLabel(closeImageIcon);
        closeButton.setPreferredSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        closeButton.setMaximumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        closeButton.setMinimumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        topBox.add(closeButton);
        topBox.add(Box.createHorizontalStrut(getMyUnit(1)));
        mainBox.add(topBox);

        JTextField channelNameField = new JTextField(CHANNEL_NAME_PLACEHOLDER);
        channelNameField.setPreferredSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelNameField.setMaximumSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelNameField.setMinimumSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelNameField.setFont(DEFAULT_FONT);
        channelNameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (channelNameField.getText().equals(CHANNEL_NAME_PLACEHOLDER)) {
                    channelNameField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (channelNameField.getText().equals("")) {
                    channelNameField.setText(CHANNEL_NAME_PLACEHOLDER);
                }
            }
        });
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        mainBox.add(channelNameField);

        JTextField channelTopicField = new JTextField(CHANNEL_TOPIC_PLACEHOLDER);
        channelTopicField.setPreferredSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelTopicField.setMaximumSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelTopicField.setMinimumSize(new Dimension(getMyUnit(16), getMyUnit(2)));
        channelTopicField.setFont(DEFAULT_FONT);
        channelTopicField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (channelTopicField.getText().equals(CHANNEL_TOPIC_PLACEHOLDER)) {
                    channelTopicField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (channelTopicField.getText().equals("")) {
                    channelTopicField.setText(CHANNEL_TOPIC_PLACEHOLDER);
                }
            }
        });
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        mainBox.add(channelTopicField);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.setPreferredSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        buttonBox.setMaximumSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        buttonBox.setMinimumSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        JButton createChannelButton = new JButton("Create channel");
        createChannelButton.setPreferredSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        createChannelButton.setMaximumSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        createChannelButton.setMinimumSize(new Dimension(getMyUnit(6), getMyUnit(2)));
        createChannelButton.setFont(DEFAULT_FONT);
        createChannelButton.addActionListener(e -> {
            if (channelNameField.getText() == CHANNEL_NAME_PLACEHOLDER || channelNameField.getText() == "") {
                return;
            }
            chatClient.createChannel(channelNameField.getText(), channelTopicField.getText());
            dispose();
        });
        // TODO onko button jotenkin isompi kuin muut jutut, kun ei mahu jutut tähän
        // ikkunaan
        buttonBox.add(createChannelButton);
        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        mainBox.add(buttonBox);

        mainBox.add(Box.createVerticalStrut(getMyUnit(1)));
        add(mainBox);
    }

}
