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

    public static void main(String[] args) throws InterruptedException {

        PlaySound.play(Sound.MOVE);
        Thread.sleep(1000);
        PlaySound.play(Sound.CAPTURE);
        Thread.sleep(1000);
        PlaySound.play(Sound.CASTLE);
        Thread.sleep(1000);
        PlaySound.play(Sound.CHECK);
        Thread.sleep(1000);
        PlaySound.play(Sound.MATE);
        Thread.sleep(1000);
        PlaySound.play(Sound.STALEMATE);
        Thread.sleep(1000);
    }
}
