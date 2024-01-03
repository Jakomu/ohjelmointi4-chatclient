package chatclient;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

import chatclient.MessageTypes.ChangeTopicMessage;
import chatclient.MessageTypes.ChatMessage;
import chatclient.MessageTypes.ErrorMessage;
import chatclient.MessageTypes.JoinMessage;
import chatclient.MessageTypes.ListChannelsMessage;
import chatclient.MessageTypes.Message;
import chatclient.MessageTypes.MessageFactory;

public class TCPClient implements Runnable {
    private ChatClientDataProvider dataProvider = null;
    private Socket socket;
    private boolean running = true;

    private PrintWriter out;
    private BufferedReader in;

    TCPClient(ChatClientDataProvider provider) {
        dataProvider = provider;
    }

    public boolean isConnected() {
        return running;
    }

    // TODO tämä pitää muokata niin että tottelee uita eikä @-merkkiä yms.
    public synchronized void postChatMessage(String message) {
        String userName = dataProvider.getNick();
        String recipientNick = null;
        String actualMessage = message;
        if (message.startsWith("@")) {
            int firstSpace = message.indexOf(' ', 0);
            if (firstSpace > 0 && firstSpace < message.length()) {
                recipientNick = message.substring(1, firstSpace);
                actualMessage = message.substring(firstSpace + 1);
            }
        }
        ChatMessage msg = new ChatMessage(LocalDateTime.now(), userName, actualMessage);
        if (null != recipientNick) {
            msg.setRecipient(recipientNick);
        }
        String jsonObjectString = msg.toJSON();
        write(jsonObjectString);
    }

    public synchronized void write(String message) {
        System.out.println("DEBUG OUT: " + message);
        out.write(message + "\n");
        out.flush();
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (socket == null) {
                    connect();
                }
                String data;
                while ((data = in.readLine()) != null) {
                    // ChatClient.println("DEBUG IN: " + data, ChatClient.colorInfo);
                    boolean continueReading = handleMessage(data);
                    if (!continueReading) {
                        break;
                    }
                }
            } catch (EOFException e) {
                // ChatClient.println("ChatSession: EOFException", ChatClient.colorError);
            } catch (IOException e) {
                // ChatClient.println("ChatSession: IOException", ChatClient.colorError);
                ErrorMessage msg = new ErrorMessage("Cannot connect: " + e.getLocalizedMessage(), true);
                dataProvider.handleReceived(msg);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }
    }

    private void connect() throws IOException, UnknownHostException {
        String hostName = dataProvider.getServer();
        int port = dataProvider.getPort();
        InetAddress hostAddress = InetAddress.getByName(hostName);
        socket = new Socket(hostAddress, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        System.out.println("Connecting to server " + hostAddress);
    }

    private boolean handleMessage(String data) {
        Message received = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            received = MessageFactory.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            received = new ErrorMessage("Invalid JSON message from client");
        }
        return dataProvider.handleReceived(received);
    }

    public void close() {
        running = false;
        if (null != socket) {
            try {
                socket.close();
                if (null != in)
                    in.close();
                if (null != out)
                    out.close();
            } catch (IOException e) {
                // nada
            } finally {
                in = null;
                out = null;
                socket = null;
                dataProvider.connectionClosed();
            }
        }
    }

    public void changeChannelTo(String channel) {
        JoinMessage msg = new JoinMessage(channel);
        String jsonObjectString = msg.toJSON();
        write(jsonObjectString);
    }

    public void changeTopicTo(String topic) {
        ChangeTopicMessage newTopic = new ChangeTopicMessage(topic);
        String jsonString = newTopic.toJSON();
        write(jsonString);
    }

    public void listChannels() {
        ListChannelsMessage listChannels = new ListChannelsMessage();
        String msg = listChannels.toJSON();
        write(msg);
    }

}
