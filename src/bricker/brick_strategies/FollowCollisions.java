package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

/**
 * Monitors collisions and removes the camera focus after a certain number of collisions.
 */
public class FollowCollisions {

    private static final int NUM_COLLISIONS_FOR_CAMERA = 4;
    private static int collisionCount;
    private static BrickerGameManager gameManager = null;

    /**
     * Initializes the FollowCollisions object.
     *
     * @param gameManager The BrickerGameManager instance to monitor collisions for.
     */
    public FollowCollisions(BrickerGameManager gameManager) {
        FollowCollisions.gameManager = gameManager;
        collisionCount = gameManager.getBall().getCollisionCounter();
    }

    /**
     * Checks collisions and removes the camera focus after a certain number of collisions.
     */
    public static void checkCollisions() {
        if (gameManager == null) return;
        if (gameManager.getBall().getCollisionCounter() - collisionCount ==
                NUM_COLLISIONS_FOR_CAMERA) {
            gameManager.setCamera(null);
        }
    }
}
