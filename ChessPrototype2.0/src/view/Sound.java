package view;

import java.io.File;
import java.net.URISyntaxException;

public enum Sound {
    MOVE("/move.wav"),
    CAPTURE("/capture.wav"),
    CASTLE("/castle.wav"),
    CHECK("/check.wav"),
    STALEMATE("/end.wav"),
    MATE("/end.wav");

    private File file;
    Sound(String file){
        try {
            this.file = new File(this.getClass().getResource(file).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
