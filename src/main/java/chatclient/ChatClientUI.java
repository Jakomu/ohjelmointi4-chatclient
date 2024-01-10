package chatclient;

import static chatclient.resources.constants.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    protected JTextArea messagePanel = new JTextArea();
    private JScrollPane scrollPane;
    private JTextField textarea;
    private CustomTooltip dropdownTooltip;
    private CustomTooltip settingsTooltip;
    private CustomTooltip textareaTooltip;

    public ChatClientUI(ChatClient chatClient) {
        super();
        this.chatClient = chatClient;

        root.setSize(new Dimension(1280, 720));
        root.setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();

        // Ylin taso
        Box mainBox = Box.createVerticalBox();
        mainBox.setSize(1280, 720);
        mainBox.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Yläpaneeli
        Box upperPanel = Box.createHorizontalBox();
        upperPanel.setPreferredSize(new Dimension(1040, 40));
        upperPanel.setMaximumSize(new Dimension(2000, 80));
        upperPanel.setMinimumSize(new Dimension(400, 40));

        // Kanavavalikko
        Box channelsArea = Box.createVerticalBox();
        dropdown = new JComboBox<String>();
        dropdown.setPreferredSize(new Dimension(500, 40));
        dropdown.setMaximumSize(new Dimension(2000, 40));
        dropdown.setMinimumSize(new Dimension(220, 40));

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
        upperPanel.add(Box.createHorizontalStrut(15));

        // Ikonit
        Box iconArea = Box.createHorizontalBox();
        iconArea.add(Box.createHorizontalStrut(5));
        URL helpImgUrl = getClass().getResource("resources/icons/question-30.png");
        ImageIcon helpImageIcon = new ImageIcon(helpImgUrl);
        JLabel helpIcon = new JLabel(helpImageIcon);
        helpIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                toggleTooltips();
            }
        });

        iconArea.add(helpIcon);
        iconArea.add(Box.createHorizontalStrut(10));
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
        iconArea.add(Box.createHorizontalStrut(10));
        upperPanel.add(iconArea);

        mainBox.add(upperPanel);
        mainBox.add(Box.createVerticalStrut(40));

        // Chat-paneeli
        chatPanel.setPreferredSize(new Dimension(800, 440));
        chatPanel.setMaximumSize(new Dimension(2000, 2000));
        chatPanel.setMinimumSize(new Dimension(400, 200));
        chatPanel.setBorder(new RoundedBorder(25));
        messagePanel.setFont(DEFAULT_FONT);
        messagePanel.setLineWrap(true);
        messagePanel.setWrapStyleWord(true);
        messagePanel.setEditable(false);
        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatPanel.add(scrollPane);
        mainBox.add(chatPanel);
        mainBox.add(Box.createVerticalStrut(40));

        // Alapaneeli
        Box lowerPanel = Box.createHorizontalBox();
        lowerPanel.setPreferredSize(new Dimension(1040, 100));
        lowerPanel.setMinimumSize(new Dimension(400, 100));
        lowerPanel.setMaximumSize(new Dimension(2000, 100));
        textarea = new JTextField(TEXTAREA_PLACEHOLDER);
        textarea.setFont(DEFAULT_FONT);
        textarea.setBorder(new RoundedBorder(15));
        textarea.setPreferredSize(new Dimension(500, 80));
        textarea.setMaximumSize(new Dimension(2000, 80));
        textarea.setMinimumSize(new Dimension(220, 40));
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

        lowerPanel.add(textarea);
        lowerPanel.add(Box.createHorizontalStrut(30));
        JButton sendButton = new JButton("Lähetä");
        sendButton.setPreferredSize(new Dimension(100, 80));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textarea.getText().equals(TEXTAREA_PLACEHOLDER)) {
                    if (textarea.getText().startsWith("@")) {
                        chatClient.sendPrivateMessage(textarea.getText());
                    } else {
                        chatClient.sendMessage(textarea.getText());
                    }
                    textarea.setText(TEXTAREA_PLACEHOLDER);
                }
            }
        });

        lowerPanel.add(sendButton);
        lowerPanel.add(Box.createHorizontalStrut(30));
        mainBox.add(lowerPanel);

        // Tooltips
        dropdownTooltip = new CustomTooltip(DROPDOWN_TOOLTIP_TEXT, new Dimension(360, 40));
        settingsTooltip = new CustomTooltip(SETTINGS_TOOLTIP_TEXT, new Dimension(380, 40));
        textareaTooltip = new CustomTooltip(MESSAGEAREA_TOOLTIP_TEXT, new Dimension(720, 40));

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

    public void addMessage(String messageText) {
        messagePanel.append(messageText + "\n");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            }
        });
    }

    public void clearMessages() {
        messagePanel.setText("");
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
            Point dropdownLocation = dropdown.getLocationOnScreen();
            dropdownLocation.x += 40;
            Point settingsLocation = settingsIcon.getLocationOnScreen();
            settingsLocation.x -= 370;
            settingsLocation.y -= 10;
            Point textareaLocation = textarea.getLocationOnScreen();
            textareaLocation.x += 40;
            textareaLocation.y -= 40;

            dropdownTooltip.showTooltip(dropdownLocation);
            settingsTooltip.showTooltip(settingsLocation);
            textareaTooltip.showTooltip(textareaLocation);
        } else {
            dropdownTooltip.hideTooltip();
            settingsTooltip.hideTooltip();
            textareaTooltip.hideTooltip();
        }
    }
}
