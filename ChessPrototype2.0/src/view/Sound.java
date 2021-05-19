package view;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Enum to provide some constants for sounds
 */

public enum Sound {
    MOVE("/move.wav"),
    CAPTURE("/capture.wav"),
    CASTLE("/castle.wav"),
    CHECK("/check.wav"),
    STALEMATE("/end.wav"),
    MATE("/end.wav");

    private File file;

    /**
     * Opens the existing .wav file
     * @param file
     */
    Sound(String file){
        try {
            this.file = new File(this.getClass().getResource(file).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the audio file
     * @return
     */
    public File getFile() {
        return file;
    }
}
