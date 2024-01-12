package chatclient;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    private float soundLevel = 0f;
    private boolean mute = false;

    public AudioPlayer() {

    }

    public void setSoundLevel(float soundLevel) {
        this.soundLevel = soundLevel;
    }

    public float getSoundLevel() {
        return soundLevel;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public boolean getMute() {
        return mute;
    }

    public void playNotification() {
        if (!mute) {
            try {
                URL audioUrl = getClass().getResource("/audio/notification.wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                gainControl.setValue(soundLevel);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

    }
}