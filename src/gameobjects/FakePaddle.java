package gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class FakePaddle extends Paddle {
    private static volatile FakePaddle instance;
    private final GameObjectCollection gameObjects;
    private int collisions = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener
     * @param windowDimensions
     * @param gameObjects
     */
    private FakePaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                       Vector2 windowDimensions, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.gameObjects = gameObjects;
    }

    public static void getInstance(ImageRenderable paddleImage, UserInputListener inputListener,
                                   Vector2 windowDimensions, BrickerGameManager gameManager) {
        if (instance == null) {
            synchronized (FakePaddle.class) {
                if (instance == null) {
                    instance = new FakePaddle(Vector2.ZERO, Paddle.DEFAULT_SIZE, paddleImage, inputListener,
                            windowDimensions, gameManager.getGameObjects());
                    instance.setCenter(new Vector2(windowDimensions.x(), windowDimensions.y()).mult(0.5f));
                    gameManager.addObject(instance);
                }
            }
        }
    }


    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) collisions++;
        if (collisions == 4) {
            this.gameObjects.removeGameObject(instance);
            instance = null;
        }
    }
}
