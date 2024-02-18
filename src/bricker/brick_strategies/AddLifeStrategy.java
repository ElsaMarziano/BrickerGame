package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import bricker.gameobjects.Heart;

/**
 * A strategy class that implements the behavior when a game object collides with a brick,
 * adding life to the player.
 */
public class AddLifeStrategy extends BasicCollisionStrategy {
    private static final String PATH_OF_HEART_PICTURE = "assets/heart.png";
    private final ImageRenderable imageRenderable;
    private final Counter lifeCounter;
    private final BrickerGameManager gameManager;

    /**
     * Constructs an AddLifeStrategy instance.
     *
     * @param gameManager The game manager instance.
     * @param counters    The counters containing game stats.
     * @param imageReader The image reader for loading images.
     */
    public AddLifeStrategy(BrickerGameManager gameManager, Counters counters,
                           ImageReader imageReader) {
        super(gameManager, counters.brickCounter);
        this.gameManager = gameManager;
        this.imageRenderable = imageReader.readImage(PATH_OF_HEART_PICTURE,
                true);
        this.lifeCounter = counters.lifeCounter;
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
                this.gameManager, this.gameManager.getWindowDimensions());
        this.gameManager.addObject(heart);
    }
}
