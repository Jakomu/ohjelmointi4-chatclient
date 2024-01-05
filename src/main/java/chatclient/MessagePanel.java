package chatclient;

import javax.swing.*;

import static chatclient.resources.constants.DEFAULT_FONT;

import java.awt.*;

public class MessagePanel extends JPanel {
    private JPanel messageContainer;
    private JScrollPane scrollPane;

    // TODO poista ylimääräiset kommentit
    public MessagePanel() {
        setLayout(new BorderLayout()); // Asetetaan pääelementin asettelu

        // Luodaan viestisäiliö ja määritetään BoxLayout pystysuuntaiseen asetteluun
        messageContainer = new JPanel();
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));

        // Lisätään vierityspalkit viestisäiliölle
        scrollPane = new JScrollPane(messageContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER); // Lisätään vierityspalkit pääelementtiin
    }

    // Metodi uuden viestin lisäämiseen
    public void addMessage(String messageText) {
        // Luodaan uusi JLabel viestille ja lisätään se säiliöön
        JLabel messageLabel = new JLabel(messageText);
        messageLabel.setFont(DEFAULT_FONT);
        messageContainer.add(messageLabel);
        messageContainer.revalidate(); // Päivitetään säiliön näkymä
        messageContainer.repaint();
        // Vieritetään säiliö viimeisimmän viestin kohdalle
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            }
        });
    }

    public void clearMessages() {
        messageContainer.removeAll();
        messageContainer.revalidate();
        messageContainer.repaint();
    }
}
