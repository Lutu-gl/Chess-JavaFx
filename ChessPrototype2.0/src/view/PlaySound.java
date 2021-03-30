package view;

import javax.sound.sampled.*;
import java.io.IOException;

public class PlaySound {
    public static void play(Sound sound) {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(sound.getFile());
        } catch (UnsupportedAudioFileException | IOException e) {
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

    public static void main(String[] args) {

        PlaySound.play(Sound.MOVE);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
