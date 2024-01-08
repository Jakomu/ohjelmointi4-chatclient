package chatclient.resources;

import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;

public class constants {
    // Fonts
    public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    public static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    // Texts
    public static final String TEXTAREA_PLACEHOLDER = "Write your message here...";
    public static final String CHANNEL_NAME_PLACEHOLDER = "Write channel name here";
    public static final String CHANNEL_TOPIC_PLACEHOLDER = "Write channel topic here";
    public static final String NICKNAME_PLACEHOLDER = "Write your nickname here";

    // Measures
    public static final int MY_UNIT = 25;

    // This is used to make it easier to get the correct size for the components
    // from design
    public static int getMyUnit(int amount) {
        return amount * MY_UNIT;
    }
}
