package gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class represents a falling heart which will add a life if the Paddle catches it
 */
public class Heart extends GameObject {
    public static final Vector2 DEFAULT_SIZE = new Vector2(20, 20);
    private static final int MAX_HEARTS = 4;

    private final Counter lifeCounter;
    private final BrickerGameManager gameManager;
    private final Vector2 windowDimensions;

    /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param windowDimensions Window dimensions to know when the heart fell
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 Counter lifeCounter, BrickerGameManager gameManager,
                 Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.lifeCounter = lifeCounter;
        this.gameManager = gameManager;
        this.windowDimensions = windowDimensions;
        setVelocity(new Vector2(0, 100));
    }

    /**
     * Delete object if he's out of the screen
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() >= this.windowDimensions.y()) {
            this.gameManager.deleteObject(this);
        }
    }

    /**
     * Makes sure the heart collides only with the real paddle
     *
     * @param other The other GameObject.
     * @return True if collided with real paddle
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("Real Paddle"); // Collides only with real paddle
    }

    /**
     * Increments lives if collided with real paddle
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.gameManager.deleteObject(this);
        if (lifeCounter.value() == MAX_HEARTS) return;
        else {
            lifeCounter.increment();
        }

    }
}
