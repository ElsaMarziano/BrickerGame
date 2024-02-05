package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;

import java.util.Random;

public class PuckBallStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final ImageRenderable imageRenderable;
    private final Sound sound;
    public Counter counter;

    public PuckBallStrategy(BrickerGameManager gameManager, Counter counter,
                            GameHelper gameHelper) {
        super(gameManager, counter);
        this.gameManager = gameManager;
        this.counter = counter;
        this.imageRenderable = gameHelper.imageReader.readImage(
                "assets/mockBall.png", true);
        this.sound = gameHelper.soundReader.readSound(
                "assets/Bubble5_4.wav");
    }

    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        Vector2 center = brick.getCenter();
        createBall(center);
        createBall(center);
    }

    private void createBall(Vector2 center) {
        // CREATING BALL
        Ball ball = new Ball(Vector2.ZERO, Ball.DEFAULT_SIZE.mult(0.75f),
                imageRenderable, this.sound, "Puck Ball", this.gameManager);
        // Add ball object to game
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float velX = (float) Math.cos(angle);
        float velY = (float) Math.sin(angle);
        ball.setVelocity(new Vector2(velX, velY).mult(100));
        ball.setCenter(center);
        this.gameManager.addObject(ball);
        this.gameManager.addObject(ball);
    }
}
