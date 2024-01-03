package chatclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import chatclient.MessageTypes.*;

public class ChatClient extends JFrame implements ChatClientDataProvider {

    // TODO poista kaikki ylimääräset console.logit!
    // TODO käännä kaikki suomenkieliset tekstit englanniksi
    // TODO poista kaikki ylimääräiset importit
    // TODO reconnect-toiminto pyörimään jos yhteys tippuu
    private static TCPClient tcpClient;
    // TODO vaihda nämä vaikk siihen tiedostototeutukseen tms.
    private int serverPort = 10000;
    private String currentServer = "localhost";
    private String nick = "Jakomu";
    // TODO tarviiko tätä messages loppujenlopuksi mihinkään, kun viestit
    // käsitellään reaaliaikaisesti?
    private List<Message> messages = new ArrayList<Message>();

    private String[] channels = new String[0];
    private String currentTopic = "";
    // TODO siirrä mainBox tästä pois, jos sitä ei tarviikkaan päivitellä
    private Box mainBox = Box.createVerticalBox();
    private Box chatPanel = Box.createHorizontalBox();
    private Box senderArea = Box.createVerticalBox();
    private Box messageArea = Box.createVerticalBox();
    private JTextArea textarea = new JTextArea("Kirjoita tähän viestisi...", 3, 100);

    // TODO pitäskö tehä muutes serverille, että palauttaa topicin kanavan nimen
    // mukana? "main (1) - semmosta ja tämmöstä"
    private Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    public ChatClient() {
        super();
        // TODO pitäiskö tämä siirtää tästä johonkin?
        tcpClientRunner();
        // Hanki alkutiedot
        // TODO keksi parempi tapa
        // Voisko tehä vaikka niin, että tuolta funktiosta palautetaan true ja sitten
        // tehään while = false -> pyöritä tyhjää
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO pitäiskö tämän pyöriä koko ajan taustalla ja hakea kanavatiedot, vai
        // hakisko sitten jos aukasee sen kanavadropparin?
        tcpClient.listChannels();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Ylin taso
        mainBox.setBorder(new EmptyBorder(40, 80, 40, 80));
        mainBox.setPreferredSize(new Dimension(1600, 900));

        // Yläpaneeli
        Box upperPanel = Box.createHorizontalBox();
        upperPanel.setPreferredSize(new Dimension(800, 50));
        upperPanel.setMaximumSize(new Dimension(2000, 50));

        // Kanavavalikko
        Box channelsArea = Box.createVerticalBox();
        JComboBox<String> dropdown = new JComboBox<String>(channels);
        dropdown.setSelectedItem(channels[findDefaultChannelIndex()]);
        channelsArea.add(dropdown);
        upperPanel.add(channelsArea);

        // Ikonit
        // TODO nämä pitää asetella kivemmin
        Box iconArea = Box.createHorizontalBox();
        URL imgUrl = getClass().getResource("resources/icons/question-50.png");
        ImageIcon helpImageIcon = new ImageIcon(imgUrl);
        JLabel helpIcon = new JLabel(helpImageIcon);
        iconArea.add(helpIcon);
        imgUrl = getClass().getResource("resources/icons/settings-50.png");
        ImageIcon settingsImageIcon = new ImageIcon(imgUrl);
        JLabel settingsIcon = new JLabel(settingsImageIcon);
        settingsIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeChannelToBotTest();
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
        chatPanel.add(senderArea);
        chatPanel.add(Box.createHorizontalStrut(40));
        chatPanel.add(messageArea);
        mainBox.add(chatPanel);
        mainBox.add(Box.createVerticalStrut(20));

        // Alapaneeli
        Box lowerPanel = Box.createHorizontalBox();
        lowerPanel.setPreferredSize(new Dimension(1300, 120));
        lowerPanel.setMinimumSize(new Dimension(400, 120));
        lowerPanel.setMaximumSize(new Dimension(2000, 120));
        textarea.setFont(defaultFont);
        textarea.setBorder(new RoundedBorder(15));
        // JScrollPane scroller = new JScrollPane(textarea);
        lowerPanel.add(textarea);
        lowerPanel.add(Box.createHorizontalStrut(40));
        JButton sendButton = new JButton("Lähetä");
        // TODO poista tämä kunhan on testailtu
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        lowerPanel.add(sendButton);
        lowerPanel.add(Box.createHorizontalStrut(40));
        mainBox.add(lowerPanel);

        getContentPane().add(mainBox);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        System.out.println("Nyt pitäis olla GUI pystyssä!");

        // TODO pitäis päivittää kanavat säännöllisesti vai tuleeko sieltä aina viesti
        // kun tulee muutos?
        // TODO addcontainerlistener vois toimia tekemään ui-päivityksen jos tarvii
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

    // TODO poista jos ei tätä tarvita
    // public void updateUI() {
    // SwingUtilities.invokeLater(new Runnable() {
    // public void run() {
    // System.out.println("aletaan päivittämään");
    // invalidate();
    // revalidate();
    // repaint();
    // System.out.println("pitäs olla päivitetty");
    // }
    // });
    // }

    public String getServer() {
        return currentServer;
    }

    public int getPort() {
        return serverPort;
    }

    public String getNick() {
        return nick;
    }

    // TODO tälle toiminto nappiin
    public void setNick(String newNick) {
        // TODO pitää varmaan ilmottaa serverille?
        this.nick = newNick;
    }

    public boolean handleReceived(Message message) {
        JLabel sender = new JLabel();
        JLabel chatMsg = new JLabel();
        sender.setFont(defaultFont);
        chatMsg.setFont(defaultFont);
        switch (message.getType()) {
            case Message.CHAT_MESSAGE: {
                // TODO onko tämä tarpeellinen?
                if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    messages.add(message);
                    System.out.println(chatMessage.getMessage());
                    sender.setText(chatMessage.getNick() + ":");
                    chatMsg.setText(chatMessage.getMessage());
                    // TODO tähän replyviestin käsittely, jos sellanen toteutetaan
                    if (chatMessage.isDirectMessage()) {
                        sender.setForeground(Color.RED);
                    }
                }
                break;
            }

            case Message.LIST_CHANNELS: {
                ListChannelsMessage channelMessage = (ListChannelsMessage) message;
                System.out.println("kanavat tulloo!");
                List<String> list = channelMessage.getChannels();
                if (null != list) {
                    this.channels = list.toArray(new String[list.size()]);
                }
                break;
            }

            case Message.CHANGE_TOPIC: {
                ChangeTopicMessage topicMessage = (ChangeTopicMessage) message;
                // TODO Päivitä aihe siihen paikkaan, mihin se lopulta toteutetaan
                this.currentTopic = topicMessage.getTopic();
                // System.out.println(topicMessage.getTopic());
                sender.setText("Server (Topic):");
                chatMsg.setText(topicMessage.getTopic());
                break;
            }

            case Message.STATUS_MESSAGE: {
                StatusMessage statusMessage = (StatusMessage) message;
                messages.add(statusMessage);
                // System.out.println(messages);
                sender.setText("Server (Status):");
                chatMsg.setText(statusMessage.getStatus());
                break;
            }

            case Message.ERROR_MESSAGE: {
                ErrorMessage msg = (ErrorMessage) message;
                // TODO Ilmaise errori
                if (msg.requiresClientShutdown()) {
                    // TODO Sulje koko roska ehkä, tai sitten yhdistä uudelleen...
                    // System.out.println(msg.getError());
                }
                break;
            }

            default:
                System.out.println("Received message was unknown");
                // TODO poista nämä, ovat debugia varten
                sender.setText("DEBUG:");
                chatMsg.setText("Message type not regognized");
                break;
        }
        senderArea.add(sender);
        messageArea.add(chatMsg);
        chatPanel.revalidate();
        chatPanel.repaint();
        return true;
    }

    public void connectionClosed() {
        // TODO muokkaa sellaiseksi kuin halutaan
        handleReceived(new StatusMessage("Connection lost..."));
    }

    public void changeChannel(String channel) {
        clearMessages();
        JoinMessage message = new JoinMessage(channel);
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
    }

    public void changeTopic(String topic) {
        ChangeTopicMessage message = new ChangeTopicMessage(topic);
        String jsonString = message.toJSON();
        tcpClient.write(jsonString);
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

    // Apufunktioita

    // TODO poista tämä kunhan on testailtu
    public void changeChannelToBotTest() {
        changeChannel("odysseu");
    }

    public int findDefaultChannelIndex() {
        int i = 0;
        for (String channel : channels) {
            if (channel.startsWith("main (")) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public void clearMessages() {
        senderArea.removeAll();
        messageArea.removeAll();
    }

    // TODO tämä pitäs heittää tästä johonkin
    public class RoundedBorder implements Border {

        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

}