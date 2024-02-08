package gameobjects;

import brick_strategies.FollowCollisions;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {
    public static final Vector2 DEFAULT_SIZE = new Vector2(20, 20);
    private final Sound collisionSound;
    private String tag = "Normal Ball";
    private int collisionCount = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null,
     *                      in which case the GameObject will not be rendered.
     */

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, String tag) {
        super(topLeftCorner, dimensions, renderable);
        this.tag = tag;
        this.collisionSound = collisionSound;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        setVelocity(getVelocity().flipped(collision.getNormal()));
        this.collisionSound.play();
        FollowCollisions.checkCollisions();
        collisionCount++;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    public int getCollisionsCounter() {
        return collisionCount;
    }

}
