package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for defining collision strategies.
 * Implementing classes must define the behavior to execute when a collision occurs.
 */
public interface CollisionStrategy {
    /**
     * Defines the behavior to execute when a collision occurs between two game objects.
     *
     * @param brick The first game object involved in the collision.
     * @param other The second game object involved in the collision.
     */
    void onCollision(GameObject brick, GameObject other);
}
