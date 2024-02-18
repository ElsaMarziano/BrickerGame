package bricker.brick_strategies;

import bricker.gameobjects.Paddle;
import bricker.main.BrickerGameManager;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import bricker.gameobjects.FakePaddle;

/**
 * A strategy class that implements the behavior when a game object collides with a brick,
 * adding a fake paddle to the game.
 */
public class AddPaddleStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final ImageRenderable paddleImage;
    private final UserInputListener inputListener;


    /**
     * Constructs an AddPaddleStrategy instance with the specified parameters.
     *
     * @param gameManager  The game manager instance.
     * @param brickCounter The counter for tracking the number of bricks.
     * @param gameHelper   The helper class containing necessary game components
     *                     (imageReader and so on).
     */
    public AddPaddleStrategy(BrickerGameManager gameManager, Counter brickCounter,
                             GameHelper gameHelper) {
        super(gameManager, brickCounter);
        this.gameManager = gameManager;
        this.paddleImage = gameHelper.imageReader.readImage(Paddle.PATH_OF_PADDLE_PICTURE,
                false);
        this.inputListener = gameHelper.userInputListener;
    }

    /**
     * Handles the collision between a brick and another game object.
     * When a collision occurs, a FakePaddle singleton is created (if needed) and added to the game.
     *
     * @param brick The brick game object involved in the collision.
     * @param other The other game object involved in the collision.
     */
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        FakePaddle.getInstance(paddleImage, inputListener,
                gameManager);
    }
}
