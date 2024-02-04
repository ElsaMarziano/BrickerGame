package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjects;
    public Counter counter;

    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter counter) {
        this.gameObjects = gameObjects;
        this.counter = counter;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        if (this.gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) counter.decrement();
    }
}
