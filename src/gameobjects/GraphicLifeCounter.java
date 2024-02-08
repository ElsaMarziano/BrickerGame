package gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This class represents a graphic life counter for the Bricker Game
 */
public class GraphicLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final Vector2 widgetTopLeftCorner;
    private final Renderable widgetRenderable;
    GameObject[] hearts;
    BrickerGameManager gameManager;

    /**
     * Creates a new instance of a self updating Graphic life counter
     *
     * @param widgetTopLeftCorner Top corner of hearts
     * @param widgetDimensions    Size of hearts
     * @param livesCounter        Counter of remaining lives
     * @param widgetRenderable    Heart image
     * @param gameManager         Game manager to add and remove objects
     * @param numOfLives          Int of the remaining number of lives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, BrickerGameManager gameManager, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.livesCounter = livesCounter;
        this.gameManager = gameManager;
        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.widgetRenderable = widgetRenderable;
        // Initialize the hearts array
        this.hearts = new GameObject[numOfLives + 1];
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = new GameObject(new Vector2(i * 30,
                    widgetTopLeftCorner.y() - 50),
                    Heart.DEFAULT_SIZE, widgetRenderable);
            hearts[i] = heart;
            gameManager.addObject(heart, Layer.BACKGROUND);
        }
    }

    /**
     * Remove one heart from the counter
     */
    public void update() {
        this.gameManager.deleteObject(hearts[livesCounter.value()],
                Layer.BACKGROUND);
        hearts[livesCounter.value()] = null;
    }

    /**
     * Add life if the counter doesn't match with the number of hearts on the screen
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (hearts[livesCounter.value() - 1] == null) {
            this.addLife();
        }
    }

    /*
     * Add a life both to the screen and to the hearts array
     */
    private void addLife() {
        int lives = livesCounter.value();
        GameObject heart = new GameObject(new Vector2((lives - 1) * 30,
                widgetTopLeftCorner.y() - 50),
                new Vector2(20, 20), widgetRenderable);
        hearts[lives - 1] = heart;
        this.gameManager.addObject(heart, Layer.BACKGROUND);
    }

}
