package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a Fake Paddle that disappears after a certain number of collisions
 * This class is a singleton and therefore only one fake paddle can be present on the screen at any
 * given time
 */
public class FakePaddle extends Paddle {
    private static final int NUM_OF_COLLISIONS_UNTIL_REMOVE = 4;
    private static final String FAKE_PADDLE = "Fake Paddle";
    private static volatile FakePaddle instance;
    private final BrickerGameManager gameManager;
    private int collisions = 0;

    /**
     * Construct a new FakePaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object.
     *                      Can be null, in which case the GameObject will not
     *                      be rendered.
     * @param inputListener Listen to user input so as to know if to move left or right
     * @param gameManager   The manager of the game so it can add objects
     */
    private FakePaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener,
                       BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                gameManager.getWindowDimensions());
        this.gameManager = gameManager;
    }

    /**
     * @param paddleImage   the renderable for the fake paddle
     * @param inputListener listen to user input so we can move the fake paddle left and right
     * @param gameManager   so we can erase the paddle when needed
     */
    public static void getInstance(ImageRenderable paddleImage,
                                   UserInputListener inputListener,
                                   BrickerGameManager gameManager) {
        if (instance == null) {
            synchronized (FakePaddle.class) {
                if (instance == null) {
                    instance = new FakePaddle(Vector2.ZERO, DEFAULT_SIZE,
                            paddleImage, inputListener, gameManager);
                    instance.setCenter(gameManager.getWindowDimensions()
                            .mult(BrickerGameManager.MIDDLE));
                    gameManager.addObject(instance);
                }
            }
        }
    }

    /**
     * If collided with a ball, add one to the collisions. If we got to the max number of collisions
     * allowed, remove fake paddle
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) collisions++;
        if (collisions == NUM_OF_COLLISIONS_UNTIL_REMOVE) {
            this.gameManager.deleteObject(instance);
            instance = null;
        }
    }

    @Override
    public String getTag() {
        return FAKE_PADDLE;
    }
}
