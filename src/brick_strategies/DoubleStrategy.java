package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * Implements a collision strategy that combines two other collision strategies and
 * executes them in sequence.
 * It applies a maximum of three spacial things per brick.
 */
public class DoubleStrategy implements CollisionStrategy {

    private final int doubleStrategyCount;
    CollisionStrategy strategy1;
    CollisionStrategy strategy2;

    /**
     * Constructs a DoubleStrategy object with the specified parameters.
     *
     * @param gameHelper           The GameHelper object providing game-related resources
     *                              and functionality.
     * @param counters             The Counters object managing game counters.
     * @param windowDimensions     The dimensions of the game window.
     * @param gameManager          The GameManager managing the game.
     * @param doubleStrategyCount The count of double strategies applied.
     */
    public DoubleStrategy(GameHelper gameHelper,
                          Counters counters, Vector2 windowDimensions,
                          BrickerGameManager gameManager, int doubleStrategyCount) {
        this.doubleStrategyCount = doubleStrategyCount;

        // Get two strategies using the BrickStrategyFactory
        strategy1 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                windowDimensions, gameManager, doubleStrategyCount);
        strategy2 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                windowDimensions, gameManager, doubleStrategyCount);

        // Ensure that both strategies are not BasicCollisionStrategy when doubleStrategyCount is 2
        while (doubleStrategyCount == 2 && strategy1.getClass() ==
                BasicCollisionStrategy.class) {
            strategy1 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                    windowDimensions, gameManager, doubleStrategyCount);
        }
        while (doubleStrategyCount == 2 && strategy2.getClass() ==
                BasicCollisionStrategy.class) {
            strategy2 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                    windowDimensions, gameManager, doubleStrategyCount);
        }

    }


    /**
     * Executes the combined collision strategies.
     *
     * @param brick The first game object involved in the collision.
     * @param other The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        // Make sure we do a maximum of 3 cool things per brick
        if (doubleStrategyCount <= 3) {
            this.strategy1.onCollision(brick, other);
            if (doubleStrategyCount <= 2) this.strategy2.onCollision(brick, other);
        }

    }
}
