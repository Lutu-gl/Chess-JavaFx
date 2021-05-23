package view;

import model.Chessboard;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Class to play sounds.
 */
public class PlaySound {
    /**
     * Starts a thread which plays a sound
     * @param sound constant of Sound enum
     */
    public static void play(Sound sound) {
        if (Chessboard.getInstance().debug) return;
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(sound.getInputStream()));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            assert clip != null;
            clip.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip.start();
    }
}
