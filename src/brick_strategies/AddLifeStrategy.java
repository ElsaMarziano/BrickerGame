package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Counters;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Heart;

public class AddLifeStrategy extends BasicCollisionStrategy {
    private final ImageRenderable imageRenderable;
    private final Counter lifeCounter;
    private final Vector2 windowDimensions;
    private final BrickerGameManager gameManager;

    public AddLifeStrategy(BrickerGameManager gameManager, Counters counters, ImageReader imageReader,
                           Vector2 windowDimensions) {
        super(gameManager, counters.brickCounter);
        this.gameManager = gameManager;
        this.imageRenderable = imageReader.readImage("assets/heart.png", true);
        this.lifeCounter = counters.lifeCounter;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        Heart heart = new Heart(brick.getCenter(), new Vector2(20, 20), this.imageRenderable, this.lifeCounter,
                this.gameManager, this.windowDimensions);
        this.gameManager.addObject(heart);
    }
}
