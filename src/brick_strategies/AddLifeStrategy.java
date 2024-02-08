package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Heart;
/**
 * A strategy class that implements the behavior when a game object collides with a brick,
 * adding life to the player.
 */
public class AddLifeStrategy extends BasicCollisionStrategy {
    private final ImageRenderable imageRenderable;
    private final Counter lifeCounter;
    private final Vector2 windowDimensions;
    private final BrickerGameManager gameManager;

    private final String PATH_OF_HEART_PICTURE = "assets/heart.png";

    /**
     * Constructs an AddLifeStrategy object.
     *
     * @param gameManager     The game manager instance.
     * @param counters        The counters containing game stats.
     * @param imageReader     The image reader for loading images.
     * @param windowDimensions The dimensions of the game window.
     */
    public AddLifeStrategy(BrickerGameManager gameManager, Counters counters,
                           ImageReader imageReader, Vector2 windowDimensions) {
        super(gameManager, counters.brickCounter);
        this.gameManager = gameManager;
        this.imageRenderable = imageReader.readImage(PATH_OF_HEART_PICTURE,
                true);
        this.lifeCounter = counters.lifeCounter;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision between a brick and another game object.
     * When a collision occurs, a Heart object is created and added to the game
     * to increase the player's life count.
     *
     * @param brick The brick game object involved in the collision.
     * @param other The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        Heart heart = new Heart(brick.getCenter(), Heart.DEFAULT_SIZE,
                this.imageRenderable, this.lifeCounter,
                this.gameManager, this.windowDimensions);
        this.gameManager.addObject(heart);
    }
}
