package chatclient;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import chatclient.messageTypes.*;

// TODO tarviiko JFramea tässä enää?
public class ChatClient implements ChatClientDataProvider {

    // TODO kato todo-lista
    private static TCPClient tcpClient;
    private ChatClientUI chatClientUI;
    private int serverPort = 10000;
    private String currentServer = "localhost";
    private String nick = "Jakomu";

    private String[] channels = new String[0];
    private String currentChannel = "main ";
    private Boolean channelsUpdating = false;
    private Boolean channelMenuOpen = false;
    private String currentTopic = "";

    public ChatClient() {
        super();

        chatClientUI = new ChatClientUI(this);
        tcpClientRunner();
        while (!tcpClient.isConnected()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waiting for connection...");
        }
        channelChecker();
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

    public boolean getChannelsUpdating() {
        return channelsUpdating;
    }

    public boolean getChannelMenuOpen() {
        return channelMenuOpen;
    }

    public void setChannelMenuOpen(Boolean value) {
        this.channelMenuOpen = value;
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
                            chatClientUI.addMessage(chatMessage.getNick() + " (private to "
                                    + chatMessage.directMessageRecipient() + "): " + chatMessage.getMessage());
                        } else {
                            chatClientUI.addMessage(chatMessage.getNick() + " (private): " + chatMessage.getMessage());
                        }
                    } else {
                        chatClientUI.addMessage(chatMessage.getNick() + ": " + chatMessage.getMessage());
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
                chatClientUI.clearDropdown();
                for (String channel : channels) {
                    if (channel.startsWith(trimChannelName(getCurrentChannel()))) {
                        chatClientUI.addDropdownItem(channel + " - " + getCurrentTopic());
                    } else {
                        chatClientUI.addDropdownItem(channel);
                    }
                }
                chatClientUI.selectCurrentChannelToDropdown(findChannelIndex(getCurrentChannel()));
                chatClientUI.updateDropbox();
                channelsUpdating = false;
                break;
            }

            case Message.CHANGE_TOPIC: {
                ChangeTopicMessage topicMessage = (ChangeTopicMessage) message;
                // TODO Päivitä aihe siihen paikkaan, mihin se lopulta toteutetaan UI:lle
                setCurrentTopic(topicMessage.getTopic());
                chatClientUI.addMessage("Server: " + topicMessage.getTopic());
                break;
            }

            case Message.STATUS_MESSAGE: {
                StatusMessage statusMessage = (StatusMessage) message;
                chatClientUI.addMessage("Server: " + statusMessage.getStatus());
                break;
            }

            case Message.ERROR_MESSAGE: {
                ErrorMessage msg = (ErrorMessage) message;
                chatClientUI.addMessage("Server: " + msg.getError());
                if (msg.requiresClientShutdown()) {
                    // TODO Sulje koko roska ehkä, tai sitten yhdistä uudelleen...
                }
                break;
            }

            default:
                System.out.println("Received message was unknown");
                break;
        }
        chatClientUI.updateChatPanel();
        return true;
    }

    // TODO tarviiko tätä ollenkaan?
    public void connectionClosed() {
        // TODO muokkaa sellaiseksi kuin halutaan
        handleReceived(new StatusMessage("Connection lost..."));
    }

    public void changeChannel(String channel) {
        chatClientUI.clearMessages();
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

    public void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(getNick(), message);
        String jsonString = chatMessage.toJSON();
        tcpClient.write(jsonString);
        handleReceived(chatMessage);
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