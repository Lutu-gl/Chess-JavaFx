package view;

import java.io.InputStream;

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

    private final String filename;

    /**
     * Opens the existing .wav file
     * @param file
     */
    Sound(String file){
        this.filename = file;
    }

    /**
     * Returns the audio file
     * @return
     */
    public InputStream getInputStream() {
        return this.getClass().getResourceAsStream(filename);
    }
}
