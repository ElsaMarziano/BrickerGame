package brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;


public class ChangeFocusStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final Vector2 windowDimensions;

    public ChangeFocusStrategy(BrickerGameManager gameManager, Counter counter,
                               Vector2 windowDimensions) {
        super(gameManager, counter);
        this.gameManager = gameManager;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        if (gameManager.camera() == null && other.getTag().equals("Normal Ball")) {
            gameManager.setCamera(
                    new Camera(
                            this.gameManager.ball, //object to follow
                            Vector2.ZERO, //follow the center of the object
                            this.windowDimensions.mult(1.2f), //widen the frame a bit
                            this.windowDimensions //share the window dimensions
                    )
            );
            new FollowCollisions(gameManager);
        }

    }
}
