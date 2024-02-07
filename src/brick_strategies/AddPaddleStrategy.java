package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.FakePaddle;

public class AddPaddleStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final ImageRenderable paddleImage;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    public Counter brickCounter;

    public AddPaddleStrategy(BrickerGameManager gameManager, Counter brickCounter,
                             GameHelper gameHelper, Vector2 windowDimensions) {
        super(gameManager, brickCounter);
        this.gameManager = gameManager;
        this.brickCounter = brickCounter;
        this.paddleImage = gameHelper.imageReader.readImage("assets/paddle.png",
                false);
        this.inputListener = gameHelper.userInputListener;
        this.windowDimensions = windowDimensions;

    }

    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        FakePaddle.getInstance(paddleImage, inputListener, windowDimensions,
                gameManager);
    }
}
