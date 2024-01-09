package chatclient;

import static chatclient.resources.constants.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuListener;

import chatclient.modals.*;
import chatclient.tooltips.CustomTooltip;

public class ChatClientUI extends JFrame {
    protected ChatClient chatClient;
    private JFrame root = new JFrame();
    public JComboBox<String> dropdown;
    private JLabel settingsIcon;
    private Box chatPanel = Box.createHorizontalBox();
    private MessagePanel messagePanel = new MessagePanel();
    private JTextArea textarea;
    private CustomTooltip dropdownTooltip;
    private CustomTooltip settingsTooltip;
    private CustomTooltip textareaTooltip;

    public ChatClientUI(ChatClient chatClient) {
        super();
        this.chatClient = chatClient;

        root.setSize(new Dimension(getMyUnit(64), getMyUnit(36)));
        root.setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();

        // Ylin taso
        Box mainBox = Box.createVerticalBox();
        mainBox.setSize(1600, 900);
        mainBox.setBorder(new EmptyBorder(40, 80, 40, 80));

        // Yläpaneeli
        Box upperPanel = Box
                .createHorizontalBox();
        upperPanel.setPreferredSize(new Dimension(800, 50));
        upperPanel.setMaximumSize(new Dimension(2000, 50));

        // Kanavavalikko
        Box channelsArea = Box.createVerticalBox();
        dropdown = new JComboBox<String>();

        dropdown.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && !chatClient.getChannelsUpdating()) {
                    if (e.getItem().toString() == "New channel") {
                        NewChannelModal newChannelModal = new NewChannelModal(root, "New channel", chatClient);
                        newChannelModal.setVisible(true);
                    } else {
                        chatClient.changeChannel(chatClient.trimChannelName(e.getItem().toString()));
                    }
                }
            }

        });
        dropdown.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                chatClient.setChannelMenuOpen(true);
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                chatClient.setChannelMenuOpen(false);
            }

            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                chatClient.setChannelMenuOpen(false);
            }

        });
        channelsArea.add(dropdown);
        upperPanel.add(channelsArea);

        // Ikonit
        Box iconArea = Box.createHorizontalBox();
        iconArea.add(Box.createHorizontalStrut(15));
        URL helpImgUrl = getClass().getResource("resources/icons/question-30.png");
        ImageIcon helpImageIcon = new ImageIcon(helpImgUrl);
        JLabel helpIcon = new JLabel(helpImageIcon);
        helpIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                toggleTooltips();
            }
        });
        iconArea.add(helpIcon);
        URL settingsImgUrl = getClass().getResource("resources/icons/settings-30.png");
        ImageIcon settingsImageIcon = new ImageIcon(settingsImgUrl);
        settingsIcon = new JLabel(settingsImageIcon);
        settingsIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                SettingsModal settingsModal = new SettingsModal(root, "Settings", chatClient);
                settingsModal.setVisible(true);
            }
        });
        iconArea.add(settingsIcon);
        upperPanel.add(iconArea);

        mainBox.add(upperPanel);
        mainBox.add(Box.createVerticalStrut(20));

        // Chat-paneeli
        chatPanel.setPreferredSize(new Dimension(800, 610));
        chatPanel.setMaximumSize(new Dimension(2000, 2000));
        chatPanel.setMinimumSize(new Dimension(400, 200));
        chatPanel.setBorder(new RoundedBorder(20));
        messagePanel.setFont(DEFAULT_FONT);
        chatPanel.add(messagePanel);
        mainBox.add(chatPanel);
        mainBox.add(Box.createVerticalStrut(20));

        // Alapaneeli
        Box lowerPanel = Box.createHorizontalBox();
        lowerPanel.setPreferredSize(new Dimension(1300, 120));
        lowerPanel.setMinimumSize(new Dimension(400, 120));
        lowerPanel.setMaximumSize(new Dimension(2000, 120));
        textarea = new JTextArea(TEXTAREA_PLACEHOLDER, 3, 100);
        textarea.setFont(DEFAULT_FONT);
        textarea.setBorder(new RoundedBorder(15));
        textarea.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textarea.getText().equals(TEXTAREA_PLACEHOLDER)) {
                    textarea.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (textarea.getText().equals("")) {
                    textarea.setText(TEXTAREA_PLACEHOLDER);
                }
            }
        });
        // TODO kuikkaan?
        // JScrollPane scroller = new JScrollPane(textarea);
        lowerPanel.add(textarea);
        lowerPanel.add(Box.createHorizontalStrut(40));
        JButton sendButton = new JButton("Lähetä");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textarea.getText().equals(TEXTAREA_PLACEHOLDER)) {
                    chatClient.sendMessage(textarea.getText());
                    textarea.setText(TEXTAREA_PLACEHOLDER);
                }
            }
        });
        lowerPanel.add(sendButton);
        lowerPanel.add(Box.createHorizontalStrut(40));
        mainBox.add(lowerPanel);

        // Tooltips
        dropdownTooltip = new CustomTooltip(DROPDOWN_TOOLTIP_TEXT, new Dimension(getMyUnit(14), getMyUnit(2)));
        settingsTooltip = new CustomTooltip(SETTINGS_TOOLTIP_TEXT, new Dimension(getMyUnit(4), getMyUnit(10)));
        textareaTooltip = new CustomTooltip(MESSAGEAREA_TOOLTIP_TEXT, new Dimension(getMyUnit(16), getMyUnit(2)));

        layeredPane.add(mainBox, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(dropdownTooltip, JLayeredPane.POPUP_LAYER);
        layeredPane.add(settingsTooltip, JLayeredPane.POPUP_LAYER);
        layeredPane.add(textareaTooltip, JLayeredPane.POPUP_LAYER);

        root.setLayeredPane(layeredPane);
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setVisible(true);

        // Listener to allow resizing along window even without layout manager
        layeredPane.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent evt) {

                Dimension size = root.getSize();
                mainBox.setBounds(0, 0, size.width, size.height);
            }

        });
    }

    public void addMessage(String message) {
        messagePanel.addMessage(message);
    }

    public void clearMessages() {
        messagePanel.clearMessages();
    }

    public void updateDropbox() {
        dropdown.revalidate();
        dropdown.repaint();
    }

    public void updateChatPanel() {
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public void clearDropdown() {
        dropdown.removeAllItems();
    }

    public void addDropdownItem(String item) {
        dropdown.addItem(item);
    }

    public void selectCurrentChannelToDropdown(int index) {
        dropdown.setSelectedItem(dropdown.getItemAt(index));
    }

    public void openNickModal() {
        NickModal nickModal = new NickModal(root, "Set nickname", chatClient);
        nickModal.setVisible(true);
    }

    public void toggleTooltips() {
        if (!dropdownTooltip.isVisible() && !settingsTooltip.isVisible() && !textareaTooltip.isVisible()) {
            dropdownTooltip.showTooltip(dropdown.getLocationOnScreen());
            settingsTooltip.showTooltip(settingsIcon.getLocationOnScreen());
            textareaTooltip.showTooltip(textarea.getLocationOnScreen());
        } else {
            dropdownTooltip.hideTooltip();
            settingsTooltip.hideTooltip();
            textareaTooltip.hideTooltip();
        }
    }
}
