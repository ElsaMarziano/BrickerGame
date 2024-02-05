package brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final BrickerGameManager gameManager;
    public Counter counter;

    public BasicCollisionStrategy(BrickerGameManager gameManager, Counter counter) {
        this.gameManager = gameManager;
        this.counter = counter;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        if (this.gameManager.deleteObject(brick, Layer.STATIC_OBJECTS))
            counter.decrement();
    }
}
