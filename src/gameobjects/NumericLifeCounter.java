package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;

public class NumericLifeCounter extends GameObject {
    private final GameObjectCollection gameObjects;
    private final Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.GREEN};
    private final Vector2 numericCounterTopLeftCorner;
    private final Vector2 dimensions;
    private final Counter livesCounter;
    private GameObject textObject;


    public NumericLifeCounter(Counter livesCounter, Vector2 numericCounterTopLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects) {
        super(numericCounterTopLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.numericCounterTopLeftCorner = numericCounterTopLeftCorner;
        this.dimensions = dimensions;
        this.gameObjects = gameObjects;
        initializeTextObject();
    }

    private void initializeTextObject() {
        if (livesCounter.value() == 0) return;
        TextRenderable renderedText = new TextRenderable(Integer.toString(livesCounter.value()));
        renderedText.setColor(colors[livesCounter.value() - 1]);
        textObject = new GameObject(numericCounterTopLeftCorner, dimensions, renderedText);
        this.gameObjects.addGameObject(textObject, Layer.BACKGROUND);
    }

    public void update() {
        this.gameObjects.removeGameObject(textObject, Layer.BACKGROUND);
        initializeTextObject();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.update();
    }

}
