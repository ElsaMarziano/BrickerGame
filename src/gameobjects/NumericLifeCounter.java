package gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * This class represent a numeric lif counter for the Bricker game
 */
public class NumericLifeCounter extends GameObject {
    private final BrickerGameManager gameManager;
    private final Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.GREEN};
    private final Vector2 numericCounterTopLeftCorner;
    private final Vector2 dimensions;
    private final Counter livesCounter;
    private GameObject textObject;

    /**
     * Creates a new Numerice life counter instance
     *
     * @param livesCounter                Counter of the remaining lives
     * @param numericCounterTopLeftCorner Position of the counter on the screen
     * @param dimensions                  Size of the counter
     * @param gameManager                 Game Manager to update counter
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 numericCounterTopLeftCorner, Vector2 dimensions,
                              BrickerGameManager gameManager) {
        super(numericCounterTopLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.numericCounterTopLeftCorner = numericCounterTopLeftCorner;
        this.dimensions = dimensions;
        this.gameManager = gameManager;
        initializeTextObject();
    }

    /*
     * Initialize the text object with the right color
     */
    private void initializeTextObject() {
        if (livesCounter.value() == 0) return;
        TextRenderable renderedText = new TextRenderable(Integer.toString(livesCounter.value()));
        renderedText.setColor(colors[livesCounter.value() - 1]);
        textObject = new GameObject(numericCounterTopLeftCorner, dimensions, renderedText);
        this.gameManager.addObject(textObject, Layer.BACKGROUND);
    }

    /**
     * Update counter when needed
     */
    public void update() {
        this.gameManager.deleteObject(textObject, Layer.BACKGROUND);
        initializeTextObject();
    }

    /**
     * Update text if doesn't match the counter
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
        this.update();
    }

}
