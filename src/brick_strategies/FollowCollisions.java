package brick_strategies;

import bricker.main.BrickerGameManager;

public class FollowCollisions {

    private static final int NUM_COLLISIONS_FOR_CAMERA = 4;
    private static int collisionCount;
    private static BrickerGameManager gameManager = null;

    public FollowCollisions(BrickerGameManager gameManager) {
        FollowCollisions.gameManager = gameManager;
        collisionCount = gameManager.ball.getCollisionsCounter();
    }

    public static void checkCollisions() {
        if (gameManager == null) return;
        if (gameManager.ball.getCollisionsCounter() - collisionCount == NUM_COLLISIONS_FOR_CAMERA) {
            gameManager.setCamera(null);
        }
    }
}
