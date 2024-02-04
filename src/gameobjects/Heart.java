package gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {
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
     * @param windowDimensions
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter lifeCounter,
                 BrickerGameManager gameManager, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.lifeCounter = lifeCounter;
        this.gameManager = gameManager;
        this.windowDimensions = windowDimensions;
        setVelocity(new Vector2(0, 100));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() >= this.windowDimensions.y()) {
            this.gameManager.deleteObject(this);
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("Real Paddle"); // Collides only with real paddle
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.gameManager.deleteObject(this);
        if (lifeCounter.value() == 4) return;
        else {
            lifeCounter.increment();
        }

    }
}
