package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

public class DoubleStrategy implements CollisionStrategy {

    private final int doubleStrategyCount;
    CollisionStrategy strategy1;
    CollisionStrategy strategy2;

    public DoubleStrategy(GameHelper gameHelper,
                          Counters counters, Vector2 windowDimensions,
                          BrickerGameManager gameManager, int doubleStrategyCount) {
        this.doubleStrategyCount = doubleStrategyCount;

        strategy1 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                windowDimensions, gameManager, doubleStrategyCount);
        strategy2 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                windowDimensions, gameManager, doubleStrategyCount);

        while (doubleStrategyCount == 2 && strategy1.getClass() == BasicCollisionStrategy.class) {
            strategy1 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                    windowDimensions, gameManager, doubleStrategyCount);
        }
        while (doubleStrategyCount == 2 && strategy2.getClass() == BasicCollisionStrategy.class) {
            strategy2 = BrickStrategyFactory.getStrategy(gameHelper, counters,
                    windowDimensions, gameManager, doubleStrategyCount);
        }

    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        // Make sure we do a maximum of 3 cool things per brick
        if (doubleStrategyCount <= 3) {
            this.strategy1.onCollision(brick, other);
            if (doubleStrategyCount <= 2) this.strategy2.onCollision(brick, other);
        }

    }
}
