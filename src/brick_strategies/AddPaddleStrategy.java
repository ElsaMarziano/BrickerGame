package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.FakePaddle;

/**
 * A strategy class that implements the behavior when a game object collides with a brick,
 * adding a fake paddle to the game.
 */
public class AddPaddleStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final ImageRenderable paddleImage;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final String PATH_OF_PADDLE_PICTURE = "assets/paddle.png";

    public Counter brickCounter;


    /**
     * Constructs an AddPaddleStrategy object with the specified parameters.
     *
     * @param gameManager     The game manager instance.
     * @param brickCounter    The counter for tracking the number of bricks.
     * @param gameHelper      The helper class containing necessary game components
     *                        (imageReader and so on).
     * @param windowDimensions The dimensions of the game window.
     */
    public AddPaddleStrategy(BrickerGameManager gameManager, Counter brickCounter,
                             GameHelper gameHelper, Vector2 windowDimensions) {
        super(gameManager, brickCounter);
        this.gameManager = gameManager;
        this.brickCounter = brickCounter;
        this.paddleImage = gameHelper.imageReader.readImage(PATH_OF_PADDLE_PICTURE,
                false);
        this.inputListener = gameHelper.userInputListener;
        this.windowDimensions = windowDimensions;

    }
    /**
     * Handles the collision between a brick and another game object.
     * When a collision occurs, a FakePaddle instance is created and added to the game.
     *
     * @param brick The brick game object involved in the collision.
     * @param other The other game object involved in the collision.
     */
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        FakePaddle.getInstance(paddleImage, inputListener, windowDimensions,
                gameManager);
    }
}
