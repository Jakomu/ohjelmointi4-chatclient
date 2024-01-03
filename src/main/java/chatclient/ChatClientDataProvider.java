package chatclient;

import chatclient.MessageTypes.Message;

public interface ChatClientDataProvider {
    String getServer();

    int getPort();

    String getNick();

    boolean handleReceived(Message message);

    void connectionClosed();
}