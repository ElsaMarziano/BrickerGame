package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import bricker.main.GameHelper;
import danogl.util.Vector2;

import java.util.Random;

public class BrickStrategyFactory {

    private static final int NUM_STRATEGIES = 10;  // Adjust this based on the total number of strategies

    public static CollisionStrategy getStrategy(GameHelper gameHelper,
                                                Counters counters, Vector2 windowDimensions,
                                                BrickerGameManager gameManager, int doubleStrategyCount) {
        // Use a random number generator to choose between the possible brick strategies
        int strategyIndex = new Random().nextInt(NUM_STRATEGIES);

        return switch (strategyIndex) {
            case 0, 1, 2, 3, 4 -> new BasicCollisionStrategy(gameManager, counters.brickCounter);
            case 5 -> new PuckBallStrategy(gameManager, counters.brickCounter, gameHelper);
            case 6 -> new AddPaddleStrategy(gameManager, counters.brickCounter, gameHelper, windowDimensions);
            case 7 -> new ChangeFocusStrategy(gameManager, counters.brickCounter, windowDimensions);
            case 8 -> new AddLifeStrategy(gameManager, counters, gameHelper.imageReader,
                    windowDimensions);
            case 9 -> new DoubleStrategy(gameHelper,
                    counters, windowDimensions, gameManager, doubleStrategyCount + 1);
            default -> throw new IllegalStateException("Unexpected value: " + strategyIndex);
        };
    }
}
