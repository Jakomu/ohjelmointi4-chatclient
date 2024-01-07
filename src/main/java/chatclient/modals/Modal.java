package chatclient.modals;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

import chatclient.ChatClient;

public class Modal extends JDialog {
    public Modal(JFrame parent, Dimension size, String title, ChatClient chatClient) {
        super(parent, title, true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(size);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
