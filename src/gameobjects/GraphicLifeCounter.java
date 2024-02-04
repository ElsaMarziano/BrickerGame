package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final Vector2 widgetTopLeftCorner;
    private final Renderable widgetRenderable;
    GameObject[] hearts;
    GameObjectCollection gameObjects;

    public GraphicLifeCounter(danogl.util.Vector2 widgetTopLeftCorner, danogl.util.Vector2 widgetDimensions, danogl.util.Counter livesCounter, danogl.gui.rendering.Renderable widgetRenderable, danogl.collisions.GameObjectCollection gameObjectsCollection, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjectsCollection;
        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.widgetRenderable = widgetRenderable;
        this.hearts = new GameObject[numOfLives + 1]; // Initialize the hearts array
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = new GameObject(new Vector2(i * 30, widgetTopLeftCorner.y() - 50),
                    Heart.DEFAULT_SIZE, widgetRenderable);
            hearts[i] = heart;
            gameObjects.addGameObject(heart, Layer.BACKGROUND);
        }
    }

    public void update() {
        this.gameObjects.removeGameObject(hearts[livesCounter.value()], Layer.BACKGROUND);
        hearts[livesCounter.value()] = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (hearts[livesCounter.value() - 1] == null) {
            this.addLife();
        }
    }

    public void addLife() {
        int lives = livesCounter.value();
        GameObject heart = new GameObject(new Vector2((lives - 1) * 30, widgetTopLeftCorner.y() - 50),
                new Vector2(20, 20), widgetRenderable);
        hearts[lives - 1] = heart;
        gameObjects.addGameObject(heart, Layer.BACKGROUND);
    }

}
