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
        super(parent, new Dimension(getMyUnit(25), getMyUnit(21)), title, chatClient);

        Box settingsBox = Box.createVerticalBox();

        settingsBox.add(Box.createVerticalStrut(getMyUnit(1)));
        Box topBox = Box.createHorizontalBox();
        topBox.setPreferredSize(new Dimension(getMyUnit(24), getMyUnit(2)));
        topBox.setMaximumSize(new Dimension(getMyUnit(24), getMyUnit(2)));
        topBox.setMinimumSize(new Dimension(getMyUnit(24), getMyUnit(2)));
        topBox.add(Box.createHorizontalStrut(getMyUnit(3)));
        JLabel settingsTitle = new JLabel(title);
        settingsTitle.setFont(TITLE_FONT);
        topBox.add(Box.createHorizontalGlue());
        topBox.add(settingsTitle);
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
        settingsBox.add(topBox);

        Box layoutBox = Box.createHorizontalBox();
        layoutBox.add(Box.createHorizontalStrut(getMyUnit(5)));
        Box settingsInputBox = Box.createVerticalBox();

        // Nick setting
        Box nickBox = Box.createVerticalBox();
        nickBox.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        nickBox.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        nickBox.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        JLabel nickLabel = new JLabel("Change nickname:");
        nickLabel.setFont(DEFAULT_FONT);
        nickLabel.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        nickLabel.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        nickLabel.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        // nickLabel.setAlignmentX(LEFT_ALIGNMENT); // TODO ei toimi
        nickBox.add(nickLabel);
        Box nickInputBox = Box.createHorizontalBox();
        JTextField nickInput = new JTextField();
        nickInput.setFont(DEFAULT_FONT);
        nickInput.setPreferredSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        nickInput.setMaximumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        nickInput.setMinimumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        nickInput.setText(chatClient.getNick());
        nickInputBox.add(nickInput);
        nickInputBox.add(Box.createVerticalStrut(getMyUnit(1)));
        JButton nickButton = new JButton("OK");
        nickButton.setFont(DEFAULT_FONT);
        nickButton.setPreferredSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        nickButton.setMaximumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        nickButton.setMinimumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        nickButton.addActionListener(e -> {
            chatClient.setNick(nickInput.getText());
        });
        nickInputBox.add(nickButton);
        nickBox.add(nickInputBox);
        settingsInputBox.add(nickBox);

        // Topic setting
        settingsInputBox.add(Box.createVerticalStrut(getMyUnit(1)));
        Box topicBox = Box.createVerticalBox();
        topicBox.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        topicBox.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        topicBox.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        JLabel topicLabel = new JLabel("Change channel topic:");
        topicLabel.setFont(DEFAULT_FONT);
        topicLabel.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        topicLabel.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        topicLabel.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        topicBox.add(topicLabel);
        Box topicInputBox = Box.createHorizontalBox();
        JTextField topicInput = new JTextField();
        topicInput.setFont(DEFAULT_FONT);
        topicInput.setPreferredSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        topicInput.setMaximumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        topicInput.setMinimumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        topicInput.setText(chatClient.getCurrentTopic());
        topicInputBox.add(topicInput);
        topicInputBox.add(Box.createVerticalStrut(getMyUnit(1)));
        JButton topicButton = new JButton("OK");
        topicButton.setFont(DEFAULT_FONT);
        topicButton.setPreferredSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        topicButton.setMaximumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        topicButton.setMinimumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        topicButton.addActionListener(e -> {
            chatClient.changeTopic(topicInput.getText());
        });
        topicInputBox.add(topicButton);
        topicBox.add(topicInputBox);
        settingsInputBox.add(topicBox);

        settingsInputBox.add(Box.createVerticalStrut(25));
        Box soundBox = Box.createVerticalBox();
        soundBox.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        soundBox.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        soundBox.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(4)));
        JLabel soundLabel = new JLabel("Sound volume:");
        soundLabel.setFont(DEFAULT_FONT);
        soundLabel.setPreferredSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        soundLabel.setMaximumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        soundLabel.setMinimumSize(new Dimension(getMyUnit(15), getMyUnit(2)));
        soundBox.add(soundLabel);
        Box soundVolumeBox = Box.createHorizontalBox();
        JSlider soundVolumeSlider = new JSlider();
        soundVolumeSlider.setPreferredSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        soundVolumeSlider.setMaximumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
        soundVolumeSlider.setMinimumSize(new Dimension(getMyUnit(12), getMyUnit(2)));
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
        soundVolumeBox.add(Box.createVerticalStrut(getMyUnit(1)));
        URL muteImgUrl = getClass().getResource("../resources/icons/mute-30.png");
        ImageIcon muteImageIcon = new ImageIcon(muteImgUrl);
        JButton muteButton = new JButton(muteImageIcon);
        muteButton.setPreferredSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        muteButton.setMaximumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
        muteButton.setMinimumSize(new Dimension(getMyUnit(2), getMyUnit(2)));
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

        settingsInputBox.add(Box.createVerticalStrut(getMyUnit(1)));
        JLabel copyright = new JLabel("©2024 Janne Komulainen");
        settingsInputBox.add(copyright);
        settingsInputBox.add(Box.createVerticalStrut(getMyUnit(1)));

        layoutBox.add(settingsInputBox);
        layoutBox.add(Box.createHorizontalStrut(getMyUnit(5)));
        settingsBox.add(layoutBox);

        add(settingsBox);
    }
}
