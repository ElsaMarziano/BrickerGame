package bricker.main;

import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;

public class GameHelper {
    public final ImageReader imageReader;
    public final SoundReader soundReader;
    public final UserInputListener userInputListener;

    public GameHelper(ImageReader imageReader, SoundReader soundReader,
                      UserInputListener userInputListener) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.userInputListener = userInputListener;
    }
}