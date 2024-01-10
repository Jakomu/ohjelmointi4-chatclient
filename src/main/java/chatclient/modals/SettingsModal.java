package chatclient.modals;

import static chatclient.resources.constants.*;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import chatclient.ChatClient;

public class SettingsModal extends Modal {

    public SettingsModal(JFrame parent, String title, ChatClient chatClient) {
        super(parent, new Dimension(500, 440), title, chatClient);

        Box settingsBox = Box.createVerticalBox();

        settingsBox.add(Box.createVerticalStrut(20));
        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(480, 40));
        topBox.setMaximumSize(new Dimension(480, 40));
        topBox.setMinimumSize(new Dimension(480, 40));
        topBox.add(Box.createHorizontalStrut(60));
        JLabel settingsTitle = new JLabel(title);
        settingsTitle.setFont(TITLE_FONT);
        topBox.add(Box.createHorizontalGlue());
        topBox.add(settingsTitle);
        topBox.add(Box.createHorizontalGlue());
        URL imgUrl = getClass().getResource("../resources/icons/close-30.png");
        ImageIcon closeImageIcon = new ImageIcon(imgUrl);
        JLabel closeButton = new JLabel(closeImageIcon);
        closeButton.setPreferredSize(new Dimension(40, 40));
        closeButton.setMaximumSize(new Dimension(40, 40));
        closeButton.setMinimumSize(new Dimension(40, 40));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        topBox.add(closeButton);
        topBox.add(Box.createHorizontalStrut(20));
        settingsBox.add(topBox);

        Box layoutBox = Box.createHorizontalBox();
        layoutBox.add(Box.createHorizontalStrut(100));
        Box settingsInputBox = Box.createVerticalBox();

        // Nick setting
        Box nickBox = Box.createVerticalBox();
        nickBox.setPreferredSize(new Dimension(300, 80));
        nickBox.setMaximumSize(new Dimension(300, 80));
        nickBox.setMinimumSize(new Dimension(300, 80));
        JLabel nickLabel = new JLabel("Change nickname:");
        nickLabel.setFont(DEFAULT_FONT);
        nickLabel.setPreferredSize(new Dimension(300, 40));
        nickLabel.setMaximumSize(new Dimension(300, 40));
        nickLabel.setMinimumSize(new Dimension(300, 40));
        // nickLabel.setAlignmentX(LEFT_ALIGNMENT); // TODO ei toimi
        nickBox.add(nickLabel);
        Box nickInputBox = Box.createHorizontalBox();
        JTextField nickInput = new JTextField();
        nickInput.setFont(DEFAULT_FONT);
        nickInput.setPreferredSize(new Dimension(240, 40));
        nickInput.setMaximumSize(new Dimension(240, 40));
        nickInput.setMinimumSize(new Dimension(240, 40));
        nickInput.setText(chatClient.getNick());
        nickInputBox.add(nickInput);
        nickInputBox.add(Box.createVerticalStrut(20));
        JButton nickButton = new JButton("OK");
        nickButton.setFont(DEFAULT_FONT);
        nickButton.setPreferredSize(new Dimension(40, 40));
        nickButton.setMaximumSize(new Dimension(40, 40));
        nickButton.setMinimumSize(new Dimension(40, 40));
        nickButton.addActionListener(e -> {
            chatClient.setNick(nickInput.getText());
        });
        nickInputBox.add(nickButton);
        nickBox.add(nickInputBox);
        settingsInputBox.add(nickBox);

        // Topic setting
        settingsInputBox.add(Box.createVerticalStrut(20));
        Box topicBox = Box.createVerticalBox();
        topicBox.setPreferredSize(new Dimension(300, 80));
        topicBox.setMaximumSize(new Dimension(300, 80));
        topicBox.setMinimumSize(new Dimension(300, 80));
        JLabel topicLabel = new JLabel("Change channel topic:");
        topicLabel.setFont(DEFAULT_FONT);
        topicLabel.setPreferredSize(new Dimension(300, 40));
        topicLabel.setMaximumSize(new Dimension(300, 40));
        topicLabel.setMinimumSize(new Dimension(300, 40));
        topicBox.add(topicLabel);
        Box topicInputBox = Box.createHorizontalBox();
        JTextField topicInput = new JTextField();
        topicInput.setFont(DEFAULT_FONT);
        topicInput.setPreferredSize(new Dimension(240, 40));
        topicInput.setMaximumSize(new Dimension(240, 40));
        topicInput.setMinimumSize(new Dimension(240, 40));
        topicInput.setText(chatClient.getCurrentTopic());
        topicInputBox.add(topicInput);
        topicInputBox.add(Box.createVerticalStrut(20));
        JButton topicButton = new JButton("OK");
        topicButton.setFont(DEFAULT_FONT);
        topicButton.setPreferredSize(new Dimension(40, 40));
        topicButton.setMaximumSize(new Dimension(40, 40));
        topicButton.setMinimumSize(new Dimension(40, 40));
        topicButton.addActionListener(e -> {
            chatClient.changeTopic(topicInput.getText());
        });
        topicInputBox.add(topicButton);
        topicBox.add(topicInputBox);
        settingsInputBox.add(topicBox);

        settingsInputBox.add(Box.createVerticalStrut(25));
        Box soundBox = Box.createVerticalBox();
        soundBox.setPreferredSize(new Dimension(300, 80));
        soundBox.setMaximumSize(new Dimension(300, 80));
        soundBox.setMinimumSize(new Dimension(300, 80));
        JLabel soundLabel = new JLabel("Sound volume:");
        soundLabel.setFont(DEFAULT_FONT);
        soundLabel.setPreferredSize(new Dimension(300, 40));
        soundLabel.setMaximumSize(new Dimension(300, 40));
        soundLabel.setMinimumSize(new Dimension(300, 40));
        soundBox.add(soundLabel);
        Box soundVolumeBox = Box.createHorizontalBox();
        JSlider soundVolumeSlider = new JSlider();
        soundVolumeSlider.setPreferredSize(new Dimension(240, 40));
        soundVolumeSlider.setMaximumSize(new Dimension(240, 40));
        soundVolumeSlider.setMinimumSize(new Dimension(240, 40));
        soundVolumeSlider.setMinimum(-60);
        soundVolumeSlider.setMaximum(6);
        soundVolumeSlider.setValue((int) (chatClient.getAudioPlayer().getSoundLevel()));
        soundVolumeSlider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                chatClient.getAudioPlayer().setSoundLevel(soundVolumeSlider.getValue());
                chatClient.getAudioPlayer().playNotification();
            }
        });
        if (chatClient.getAudioPlayer().getMute()) {
            soundVolumeSlider.setEnabled(false);
        } else {
            soundVolumeSlider.setEnabled(true);
        }
        soundVolumeBox.add(soundVolumeSlider);
        soundVolumeBox.add(Box.createVerticalStrut(20));
        URL muteImgUrl = getClass().getResource("../resources/icons/mute-30.png");
        ImageIcon muteImageIcon = new ImageIcon(muteImgUrl);
        JButton muteButton = new JButton(muteImageIcon);
        muteButton.setPreferredSize(new Dimension(40, 40));
        muteButton.setMaximumSize(new Dimension(40, 40));
        muteButton.setMinimumSize(new Dimension(40, 40));
        muteButton.addActionListener(e -> {
            if (chatClient.getAudioPlayer().getMute()) {
                chatClient.getAudioPlayer().setMute(false);
                soundVolumeSlider.setEnabled(true);
            } else {
                chatClient.getAudioPlayer().setMute(true);
                soundVolumeSlider.setEnabled(false);
            }
        });
        soundVolumeBox.add(muteButton);
        soundBox.add(soundVolumeBox);
        settingsInputBox.add(soundBox);

        settingsInputBox.add(Box.createVerticalStrut(20));
        JLabel copyright = new JLabel("©2024 Janne Komulainen");
        settingsInputBox.add(copyright);
        settingsInputBox.add(Box.createVerticalStrut(20));

        layoutBox.add(settingsInputBox);
        layoutBox.add(Box.createHorizontalStrut(100));
        settingsBox.add(layoutBox);

        add(settingsBox);
    }
}
