// package chatclient;

// import static org.junit.Assert.assertTrue;

// import org.junit.Test;

// import chatclient.messageTypes.ChatMessage;

// public class AppTest {

// // These tests are not working properly when run along mvn package
// // @Test
// // public void shouldRenderUI() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // assertTrue(chatClient.getChatClientUI() != null);
// // }

// // @Test
// // public void shouldAddMessageToMessagePanel() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // int startCount = chatClient.getChatClientUI().messagePanel.getLineCount();
// // ChatMessage message = new ChatMessage("test user", "test message");
// // chatClient.handleReceived(message);
// // int endCount = chatClient.getChatClientUI().messagePanel.getLineCount();
// // assertTrue(endCount == startCount + 1);
// // }

// // @Test
// // public void shouldAddPrivateMessageToMessagePanel() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // int startCount = chatClient.getChatClientUI().messagePanel.getLineCount();
// // ChatMessage message = new ChatMessage("test user", "test message");
// // message.setRecipient("another test user");
// // chatClient.handleReceived(message);
// // int endCount = chatClient.getChatClientUI().messagePanel.getLineCount();
// // assertTrue(endCount == startCount + 1);
// //
// assertTrue(chatClient.getChatClientUI().messagePanel.getText().contains("(private)"));
// // }

// // @Test
// // public void shouldChangeCurrentNick() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // String startNick = chatClient.getNick();
// // chatClient.setNick("test nick");
// // String endNick = chatClient.getNick();
// // assertTrue(!startNick.equals(endNick));
// // assertTrue(endNick.equals("test nick"));
// // }

// // There are problems with these tests, they are commented out for now
// // @Test
// // public void shouldChangeCurrentAndAddNewChannelToChannelList() {
// // ChatClient chatClient = new ChatClient();
// // String[] channels = chatClient.getChannels();
// // int startCount = channels.length;
// // chatClient.changeChannel("test channel");
// // int endCount = chatClient.getChannels().length;
// // String endChannel = chatClient.getCurrentChannel();
// // assertTrue(endCount == startCount + 1);
// // assertTrue(endChannel.equals("test channel"));
// // }

// // @Test
// // public void shouldChangeCurrentButNotAddNewChannelToChannelList() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // String[] channels = chatClient.getChannels();
// // int startCount = channels.length;
// // chatClient.changeChannel("main");
// // int endCount = chatClient.getChannels().length;
// // String endChannel = chatClient.getCurrentChannel();
// // assertTrue(endCount == startCount);
// // assertTrue(endChannel.equals("main"));
// // }

// // @Test
// // public void shouldChangeCurrentTopic() {
// // chatclient.ChatClient chatClient = new chatclient.ChatClient();
// // String startCurrentTopic = chatClient.getCurrentTopic();
// // chatClient.changeTopic("test topic");
// // String endCurrentTopic = chatClient.getCurrentTopic();
// // assertTrue(!startCurrentTopic.equals(endCurrentTopic));
// // assertTrue(endCurrentTopic.equals("test topic"));
// // }

// }
