package brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.GameHelper;
import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;

import java.util.Random;

/**
 * A strategy for creating additional balls ("Puck Balls") upon collision with some bricks.
 */
public class PuckBallStrategy extends BasicCollisionStrategy {
    private final BrickerGameManager gameManager;
    private final ImageRenderable imageRenderable;
    private final Sound sound;
    public Counter counter;

    /**
     * Constructs a PuckBallStrategy.
     *
     * @param gameManager     The BrickerGameManager instance.
     * @param counter         The counter for tracking the number of bricks.
     * @param gameHelper      The GameHelper instance for accessing resources.
     */
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

    /**
     * Handles the collision event by creating additional balls.
     *
     * @param brick The GameObject representing the brick.
     * @param other The other GameObject involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        super.onCollision(brick, other);
        Vector2 center = brick.getCenter();
        createBall(center);
        createBall(center);
    }

    /*
     * Creates a new ball at the specified center position.
     *
     * @param center The center position for the new ball.
     */
    private void createBall(Vector2 center) {
        // Creating ball
        Ball ball = new Ball(Vector2.ZERO, Ball.DEFAULT_SIZE.mult(0.75f),
                imageRenderable, this.sound, "Puck Ball");
        // Add ball object to game
        Random rand = new Random();

        // Set the velocity of the ball
        double angle = rand.nextDouble() * Math.PI;
        float velX = (float) Math.cos(angle);
        float velY = (float) Math.sin(angle);
        ball.setVelocity(new Vector2(velX, velY).mult(100));

        // Set the center position of the ball
        ball.setCenter(center);

        // Add the ball object to the game
        this.gameManager.addObject(ball);
        this.gameManager.addObject(ball);
    }
}
