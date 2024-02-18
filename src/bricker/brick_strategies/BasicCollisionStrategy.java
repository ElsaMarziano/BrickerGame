package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic collision strategy that removes a game object from the game manager
 * when a collision occurs and decrements a counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager gameManager;
    private final Counter counter;


    /**
     * Constructs a basic collision strategy.
     *
     * @param gameManager The game manager managing game objects.
     * @param counter     The counter to decrement upon collision.
     */
    public BasicCollisionStrategy(BrickerGameManager gameManager, Counter counter) {
        this.gameManager = gameManager;
        this.counter = counter;
    }

    /**
     * Handles the collision between two game objects.
     * Removes the brick game object from the game manager if it's a static object layer,
     * and decrements the counter.
     *
     * @param brick The brick game object involved in the collision.
     * @param other The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        if (this.gameManager.deleteObject(brick, Layer.STATIC_OBJECTS))
            counter.decrement();
    }
}
