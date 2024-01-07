package chatclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuListener;

import chatclient.messageTypes.*;
import chatclient.modals.*;

import static chatclient.resources.constants.*;

public class ChatClient extends JFrame implements ChatClientDataProvider {

    // TODO kato todo-lista
    private static TCPClient tcpClient;
    private int serverPort = 10000;
    private String currentServer = "localhost";
    private String nick = "Jakomu";

    private String[] channels = new String[0];
    private String currentChannel = "main ";
    private Boolean channelsUpdating = false;
    private Boolean channelMenuOpen = false;
    private String currentTopic = "";

    private JFrame root = new JFrame();
    // TODO siirrä mainBox tästä pois, jos sitä ei tarviikkaan päivitellä
    private Box mainBox = Box.createVerticalBox();
    JComboBox<String> dropdown;
    private Box chatPanel = Box.createHorizontalBox();
    private MessagePanel messagePanel = new MessagePanel();

    private JTextArea textarea = new JTextArea(TEXTAREA_PLACEHOLDER, 3, 100);

    public ChatClient() {
        super();
        tcpClientRunner();

        // Ylin taso
        mainBox.setBorder(new EmptyBorder(40, 80, 40, 80));
        mainBox.setPreferredSize(new Dimension(1600, 900));

        // Yläpaneeli
        Box upperPanel = Box.createHorizontalBox();
        upperPanel.setPreferredSize(new Dimension(800, 50));
        upperPanel.setMaximumSize(new Dimension(2000, 50));

        // Kanavavalikko
        Box channelsArea = Box.createVerticalBox();
        dropdown = new JComboBox<String>();

        dropdown.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && !channelsUpdating) {
                    if (e.getItem().toString() == "New channel") {
                        NewChannelModal newChannelModal = new NewChannelModal(root, "New channel", ChatClient.this);
                        newChannelModal.setVisible(true);
                    } else {
                        changeChannel(trimChannelName(e.getItem().toString()));
                    }
                }
            }

        });
        dropdown.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                channelMenuOpen = true;
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                channelMenuOpen = false;
            }

            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                channelMenuOpen = false;
            }

        });
        channelsArea.add(dropdown);
        upperPanel.add(channelsArea);

        // Ikonit
        Box iconArea = Box.createHorizontalBox();
        iconArea.add(Box.createHorizontalStrut(15));
        URL imgUrl = getClass().getResource("resources/icons/question-30.png");
        ImageIcon helpImageIcon = new ImageIcon(imgUrl);
        JLabel helpIcon = new JLabel(helpImageIcon);
        helpIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Help clicked");
                sendPrivateMessage("HAAAIII!", "Jakomu");
            }
        });
        iconArea.add(helpIcon);
        imgUrl = getClass().getResource("resources/icons/settings-30.png");
        ImageIcon settingsImageIcon = new ImageIcon(imgUrl);
        JLabel settingsIcon = new JLabel(settingsImageIcon);
        settingsIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                SettingsModal settingsModal = new SettingsModal(root, "Settings", ChatClient.this);
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
        // JScrollPane scroller = new JScrollPane(textarea);
        lowerPanel.add(textarea);
        lowerPanel.add(Box.createHorizontalStrut(40));
        JButton sendButton = new JButton("Lähetä");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textarea.getText().equals(TEXTAREA_PLACEHOLDER)) {
                    sendMessage();
                    textarea.setText(TEXTAREA_PLACEHOLDER);
                }
            }
        });
        lowerPanel.add(sendButton);
        lowerPanel.add(Box.createHorizontalStrut(40));
        mainBox.add(lowerPanel);

        root.add(mainBox);
        root.pack();
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setVisible(true);

        channelChecker();
        System.out.println("Nyt pitäis olla GUI pystyssä!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClient();
            }
        });
    }

    public void tcpClientRunner() {
        tcpClient = new TCPClient(this);
        new Thread(tcpClient).start();
    }

    // Päivitä kanavat 10 sekunnin välein
    public void channelChecker() {
        new Thread(new Runnable() {
            public void run() {
                tcpClient.listChannels();
                while (true) {
                    if (!channelMenuOpen) {
                        tcpClient.listChannels();
                    }
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public String getServer() {
        return currentServer;
    }

    public int getPort() {
        return serverPort;
    }

    public String getNick() {
        return nick;
    }

    public void setCurrentTopic(String topic) {
        this.currentTopic = topic;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentChannel(String channel) {
        this.currentChannel = channel;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    // TODO tälle toiminto nappiin
    public void setNick(String newNick) {
        // TODO pitää varmaan ilmottaa serverille?
        this.nick = newNick;
    }

    public boolean handleReceived(Message message) {
        switch (message.getType()) {
            case Message.CHAT_MESSAGE: {
                // TODO onko tämä tarpeellinen?
                if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    System.out.println(chatMessage.getMessage());
                    if (chatMessage.isDirectMessage()) {
                        // TODO väri viestiin?
                        if (chatMessage.getNick() == getNick()) {
                            messagePanel.addMessage(chatMessage.getNick() + " (private to "
                                    + chatMessage.directMessageRecipient() + "): " + chatMessage.getMessage());
                        } else {
                            messagePanel.addMessage(chatMessage.getNick() + " (private): " + chatMessage.getMessage());
                        }
                    } else {
                        messagePanel.addMessage(chatMessage.getNick() + ": " + chatMessage.getMessage());
                    }
                }
                break;
            }

            case Message.LIST_CHANNELS: {
                channelsUpdating = true;
                ListChannelsMessage channelMessage = (ListChannelsMessage) message;
                System.out.println("kanavat tulloo!");
                List<String> list = channelMessage.getChannels();
                list.add("New channel");
                channels = list.toArray(new String[list.size()]);
                dropdown.removeAllItems();
                for (String channel : channels) {
                    if (channel.startsWith(trimChannelName(getCurrentChannel()))) {
                        dropdown.addItem(channel + " - " + getCurrentTopic());
                    } else {
                        dropdown.addItem(channel);
                    }
                }
                dropdown.setSelectedItem(dropdown.getItemAt(findChannelIndex(getCurrentChannel())));
                dropdown.revalidate();
                dropdown.repaint();
                channelsUpdating = false;
                break;
            }

            case Message.CHANGE_TOPIC: {
                ChangeTopicMessage topicMessage = (ChangeTopicMessage) message;
                // TODO Päivitä aihe siihen paikkaan, mihin se lopulta toteutetaan UI:lle
                setCurrentTopic(topicMessage.getTopic());
                messagePanel.addMessage("Server: " + topicMessage.getTopic());
                break;
            }

            case Message.STATUS_MESSAGE: {
                StatusMessage statusMessage = (StatusMessage) message;
                messagePanel.addMessage("Server: " + statusMessage.getStatus());
                break;
            }

            case Message.ERROR_MESSAGE: {
                ErrorMessage msg = (ErrorMessage) message;
                messagePanel.addMessage("Server: " + msg.getError());
                if (msg.requiresClientShutdown()) {
                    // TODO Sulje koko roska ehkä, tai sitten yhdistä uudelleen...
                }
                break;
            }

            default:
                System.out.println("Received message was unknown");
                break;
        }
        chatPanel.revalidate();
        chatPanel.repaint();
        return true;
    }

    // TODO tarviiko tätä ollenkaan?
    public void connectionClosed() {
        // TODO muokkaa sellaiseksi kuin halutaan
        handleReceived(new StatusMessage("Connection lost..."));
    }

    public void changeChannel(String channel) {
        clearMessages();
        JoinMessage message = new JoinMessage(channel);
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
        setCurrentChannel(channel);
        tcpClient.listChannels();
    }

    public void changeTopic(String topic) {
        ChangeTopicMessage message = new ChangeTopicMessage(topic);
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
        setCurrentTopic(topic);
        tcpClient.listChannels();
    }

    public void requestChannelList() {
        ListChannelsMessage message = new ListChannelsMessage();
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
    }

    public void sendMessage() {
        ChatMessage message = new ChatMessage(getNick(), textarea.getText());
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
        handleReceived(message);
    }

    public void sendPrivateMessage(String message, String recipientNick) {
        ChatMessage privateMessage = new ChatMessage(getNick(), message);
        if (null != recipientNick) {
            privateMessage.setRecipient(recipientNick);
        }
        String jsonString = privateMessage.toJSON();
        tcpClient.write(jsonString);
        handleReceived(privateMessage);
    }

    public void createChannel(String channel, String topic) {
        changeChannel(channel);
        if (topic != null && !topic.equals("")) {
            changeTopic(topic);
        }
    }

    // Apufunktioita

    public void clearMessages() {
        messagePanel.clearMessages();
    }

    public int findChannelIndex(String wantedChannel) {
        int i = 0;
        for (String channel : channels) {
            if (channel.startsWith(wantedChannel)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public String trimChannelName(String channel) {
        if (channel.indexOf(" ") != -1) {
            return channel.substring(0, channel.indexOf(" "));
        } else {
            return channel;
        }

    }
}