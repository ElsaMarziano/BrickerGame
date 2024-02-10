package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Strategy for changing the focus of the camera upon collision with a specific object.
 * This strategy is triggered when a collision occurs between a brick and another game object.
 */
public class ChangeFocusStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final Vector2 windowDimensions;

    /**
     * Constructs a new ChangeFocusStrategy.
     *
     * @param gameManager      The game manager managing game objects.
     * @param counter          The counter tracking the number of bricks.
     * @param windowDimensions The dimensions of the game window.
     */
    public ChangeFocusStrategy(BrickerGameManager gameManager, Counter counter,
                               Vector2 windowDimensions) {
        super(gameManager, counter);
        this.gameManager = gameManager;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Performs actions when a collision between a brick and another object occurs.
     * If the collision involves a normal ball and the game manager's camera is not set,
     * it sets the camera to follow the ball.
     *
     * @param brick The brick game object involved in the collision.
     * @param other The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        if (gameManager.camera() == null && other.getTag().equals("Normal Ball")) {
            gameManager.setCamera(
                    new Camera(
                            this.gameManager.getBall(), //object to follow
                            Vector2.ZERO, //follow the center of the object
                            this.windowDimensions.mult(1.2f), //widen the frame a bit
                            this.windowDimensions //share the window dimensions
                    )
            );
            new FollowCollisions(gameManager);
        }

    }
}
