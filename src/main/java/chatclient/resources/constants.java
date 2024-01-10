package chatclient.resources;

import java.awt.Color;
import java.awt.Font;

public class constants {
    // Fonts
    public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    public static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    // Colors
    public static final Color TOOLTIP_COLOR = new Color(245, 235, 158);

    // Texts
    public static final String TEXTAREA_PLACEHOLDER = "Write your message here...";
    public static final String CHANNEL_NAME_PLACEHOLDER = "Write channel name here";
    public static final String CHANNEL_TOPIC_PLACEHOLDER = "Write channel topic here";
    public static final String NICKNAME_PLACEHOLDER = "Write your nickname here";
    public static final String DROPDOWN_TOOLTIP_TEXT = "Click to see all channels or create a new one";
    public static final String SETTINGS_TOOLTIP_TEXT = "Click to see channel, nick and sound settings";
    public static final String MESSAGEAREA_TOOLTIP_TEXT = "You can send private messages by writing \"@nickname Your message\" i.e. \"@John Hello John!\"";
}
