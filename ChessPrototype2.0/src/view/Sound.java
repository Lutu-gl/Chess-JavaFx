package view;

import java.io.File;

public enum Sound {
    MOVE("move.wav"),
    CHECK("check.wav"),
    MATE("mate.wav");

    private File file;
    Sound(String file){
        this.file = new File(file);
    }

    public File getFile() {
        return file;
    }
}
