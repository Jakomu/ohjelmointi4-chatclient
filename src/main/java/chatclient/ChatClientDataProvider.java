package chatclient;

import chatclient.messageTypes.Message;

public interface ChatClientDataProvider {
    String getServer();

    int getPort();

    String getNick();

    boolean handleReceived(Message message);

    void connectionClosed();
}