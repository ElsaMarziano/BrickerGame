package gameobjects;

import brick_strategies.FollowCollisions;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * An instance of a Ball that belongs to a game of Bricker
 */
public class Ball extends GameObject {
    public static final Vector2 DEFAULT_SIZE = new Vector2(20, 20);
    private final Sound collisionSound;
    private final BrickerGameManager manager;
    private String tag = "Normal Ball";
    private int collisionCount = 0;

    /**
     * Construct a new Ball instance
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null,
     *                       in which case the GameObject will not be rendered.
     * @param collisionSound The sound to make when the object collides with something
     */

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, BrickerGameManager manager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.manager = manager;
    }

    /**
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null,
     *                       in which case the GameObject will not be rendered.
     * @param collisionSound The sound to make when the object collides with something
     * @param tag            If needed, the tag we want the getTag function to return. Used mainly to differentiate
     *                       between normal ball and puck ball
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, String tag,
                BrickerGameManager manager) {
        super(topLeftCorner, dimensions, renderable);
        this.tag = tag;
        this.collisionSound = collisionSound;
        this.manager = manager;
    }

    /**
     * What to do when colliding with something
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        setVelocity(getVelocity().flipped(collision.getNormal()));
        this.collisionSound.play();
        FollowCollisions.checkCollisions();
        collisionCount++;
    }

    /**
     * @return The tag assigned to the instance, empty string by default
     */
    @Override
    public String getTag() {
        return this.tag;
    }

    /**
     * Get total number of collisions
     *
     * @return number of times the ball collided with something
     */
    public int getCollisionsCounter() {
        return collisionCount;
    }

}
