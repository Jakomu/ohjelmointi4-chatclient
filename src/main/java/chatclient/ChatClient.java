package chatclient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import chatclient.messageTypes.*;

public class ChatClient implements ChatClientDataProvider {

    private static TCPClient tcpClient;
    private ChatClientUI chatClientUI;
    private static int serverPort;
    private static String server;
    private String nick = "";

    private String[] channels = new String[0];
    private String currentChannel = "main ";
    private Boolean channelsUpdating = false;
    private Boolean channelMenuOpen = false;
    private String currentTopic = "";
    private AudioPlayer player = new AudioPlayer();

    public ChatClient() {
        super();

        chatClientUI = new ChatClientUI(this);
        chatClientUI.openNickModal();

        tcpClientRunner();

        System.out.println("Waiting for connection...");
        handleReceived(new StatusMessage("Waiting for connection..."));
        channelChecker();
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/chatclient/config.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("server=")) {
                    String[] parts = line.split("=");
                    if (parts.length > 1) {
                        System.out.println("Server: " + parts[1]);
                        server = parts[1];
                    }
                } else if (line.startsWith("serverport=")) {
                    String[] parts = line.split("=");
                    if (parts.length > 1) {
                        System.out.println("Serverport: " + parts[1]);
                        serverPort = Integer.parseInt(parts[1]);
                    }
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    // Update channels every 10 seconds
    public void channelChecker() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (!channelMenuOpen && tcpClient.isConnected()) {
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
        return server;
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

    public String[] getChannels() {
        return channels;
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

    public void setNick(String newNick) {
        if (nick.equals("")) {
            this.nick = newNick;
        } else {
            this.nick = newNick;
            handleReceived(new StatusMessage("You have changed your nick to " + newNick));
        }

    }

    public AudioPlayer getAudioPlayer() {
        return player;
    }

    public ChatClientUI getChatClientUI() {
        return chatClientUI;
    }

    public boolean handleReceived(Message message) {
        switch (message.getType()) {
            case Message.CHAT_MESSAGE: {
                if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    if (chatMessage.isDirectMessage()) {
                        if (chatMessage.getNick() == getNick()) {
                            chatClientUI.addMessage(chatMessage.getNick() + " (private to "
                                    + chatMessage.directMessageRecipient() + "): " + chatMessage.getMessage());
                        } else {
                            chatClientUI.addMessage(chatMessage.getNick() + " (private): " + chatMessage.getMessage());
                        }
                    } else {
                        chatClientUI.addMessage(chatMessage.getNick() + ": " + chatMessage.getMessage());
                    }
                    player.playNotification();
                }
                break;
            }

            case Message.LIST_CHANNELS: {
                channelsUpdating = true;
                ListChannelsMessage channelMessage = (ListChannelsMessage) message;
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
                channelsUpdating = false;
                break;
            }

            case Message.CHANGE_TOPIC: {
                ChangeTopicMessage topicMessage = (ChangeTopicMessage) message;
                setCurrentTopic(topicMessage.getTopic());
                chatClientUI.addMessage("Server: " + topicMessage.getTopic());
                player.playNotification();
                break;
            }

            case Message.STATUS_MESSAGE: {
                StatusMessage statusMessage = (StatusMessage) message;
                chatClientUI.addMessage("Server: " + statusMessage.getStatus());
                player.playNotification();
                break;
            }

            case Message.ERROR_MESSAGE: {
                ErrorMessage msg = (ErrorMessage) message;
                chatClientUI.addMessage("Server: " + msg.getError());
                if (msg.requiresClientShutdown()) {
                }
                player.playNotification();
                break;
            }

            default:
                System.out.println("Received message was unknown");
                break;
        }
        return true;
    }

    public void connectionClosed() {
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

    public void sendPrivateMessage(String message) {
        ChatMessage privateMessage;
        String recipientNick = null;
        String actualMessage = null;
        int firstSpace = message.indexOf(' ', 0);
        if (firstSpace > 0 && firstSpace < message.length()) {
            recipientNick = message.substring(1, firstSpace);
            actualMessage = message.substring(firstSpace + 1);
        }
        if (null != recipientNick) {
            privateMessage = new ChatMessage(getNick(), actualMessage);
            privateMessage.setRecipient(recipientNick);
        } else {
            privateMessage = new ChatMessage(getNick(), message);
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