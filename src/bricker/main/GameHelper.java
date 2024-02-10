package bricker.main;

import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;


/**
 * This class contains useful things belonging to BrickerGameManager, and is mainly intended for clarity
 */
public class GameHelper {
    /**
     * A self-explanatory image reader
     */
    public final ImageReader imageReader;
    /**
     * A self-explanatory sound reader
     */
    public final SoundReader soundReader;
    /**
     * A self-explanatory input listener
     */

    public final UserInputListener userInputListener;

    /**
     * Creates an instance of GameHelper
     *
     * @param imageReader       reads images
     * @param soundReader       reads sounds
     * @param userInputListener listen to user input
     */
    public GameHelper(ImageReader imageReader, SoundReader soundReader,
                      UserInputListener userInputListener) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.userInputListener = userInputListener;
    }
}