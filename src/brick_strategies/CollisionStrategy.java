package brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject brick, GameObject other);
}
