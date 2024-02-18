package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import bricker.main.GameHelper;

import java.util.Random;

/**
 * Factory class for creating different types of brick collision strategies.
 */
public class BrickStrategyFactory {

    // Adjust this based on the total number of strategies
    private static final int NUM_STRATEGIES = 10;

    /**
     * Default constructor for a brick strategy factory
     */
    public BrickStrategyFactory() {
    }

    /**
     * Get a random brick collision strategy.
     *
     * @param gameHelper          The game helper containing resources and utilities.
     * @param counters            The counters tracking game metrics.
     * @param gameManager         The game manager managing game objects.
     * @param doubleStrategyCount The count of double strategies used.
     * @return A collision strategy for bricks.
     */
    public static CollisionStrategy getStrategy(GameHelper gameHelper,
                                                Counters counters,
                                                BrickerGameManager gameManager,
                                                int doubleStrategyCount) {
        // Use a random number generator to choose between the
        // possible brick strategies
        int strategyIndex = new Random().nextInt(NUM_STRATEGIES);

        return switch (strategyIndex) {
            case 0, 1, 2, 3, 4 -> new BasicCollisionStrategy(gameManager,
                    counters.brickCounter);
            case 5 -> new PuckBallStrategy(gameManager, counters.brickCounter,
                    gameHelper);
            case 6 -> new AddPaddleStrategy(gameManager, counters.brickCounter,
                    gameHelper);
            case 7 -> new ChangeFocusStrategy(gameManager, counters.brickCounter);
            case 8 -> new AddLifeStrategy(gameManager, counters,
                    gameHelper.imageReader);
            case 9 -> new DoubleStrategy(gameHelper,
                    counters, gameManager,
                    doubleStrategyCount + 1);
            default -> throw new IllegalStateException("Unexpected value: " +
                    strategyIndex);
        };
    }
}
