package bricker.main;

import brick_strategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    private static final int WALL_THICKNESS = 40;
    private static final int INITIAL_LIVES = 3;
    private static final String LOSE_MASSAGE = "You lose! Play again?";
    private static final String WIN_MASSAGE = "You win! Play again?";

    public Counter brickCounter = new Counter(0);
    public Counter lives = new Counter(INITIAL_LIVES);
    private final Counters counters = new Counters(lives, brickCounter);
    public Ball ball;
    public int rowsBricks = 7;
    public int columnsBricks = 8;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private GraphicLifeCounter hearts;
    private NumericLifeCounter numericCounter;
    private GameHelper gameHelper;
    private final UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowSize,
                              UserInputListener inputListener) {
        super(windowTitle, windowSize);
        this.inputListener = inputListener;
    }

    public static void main(String[] args) {
        Vector2 windowSize= new Vector2(700, 500);
        new BrickerGameManager("Bricker", windowSize,
                ).run();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.gameObjects();
        String prompt = "";
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = WIN_MASSAGE;
        }else if (lives.value() == 0) {
            prompt = LOSE_MASSAGE;
        } else if (brickCounter.value() == 0) {
            prompt = WIN_MASSAGE;
        }
        if (!prompt.isEmpty() && windowController.openYesNoDialog(prompt)) {
            lives.reset();
            lives.increaseBy(3);
            windowController.resetGame();
        } else if (!prompt.isEmpty()) {
            windowController.closeWindow();
        }
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.gameHelper = new GameHelper(imageReader, soundReader, inputListener);
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg",
                        false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // Later purposes
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        createWalls(windowDimensions);
        createBricks(windowDimensions);

        hearts = new GraphicLifeCounter(new Vector2(60,
                windowDimensions.y() - 10), new Vector2(30, 30),
                lives, imageReader.readImage("assets/heart.png",
                true),
                this.gameObjects(), lives.value());
        numericCounter = new NumericLifeCounter(lives,
                new Vector2(windowDimensions.x() - 50,
                windowDimensions.y() - 50), new Vector2(30, 30),
                this.gameObjects());

        this.gameObjects().addGameObject(hearts, Layer.BACKGROUND);
        this.gameObjects().addGameObject(numericCounter, Layer.BACKGROUND);

        // CREATING BALL
        ball = new Ball(Vector2.ZERO, Ball.DEFAULT_SIZE,
                imageReader.readImage("assets/ball.png",
                        true),
                soundReader.readSound("assets/Bubble5_4.wav"),
                this);
        // Add ball object to game
        Random rand = new Random();
        int velX = 1, velY = 1;
        if (rand.nextBoolean()) velX = -1;
        if (rand.nextBoolean()) velY = -1;
        ball.setVelocity(new Vector2(velX, velY).mult(100));
        ball.setCenter(windowDimensions.mult(0.5f));
        this.addObject(ball);

        // CREATING PADDLE
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - 50));
        this.gameObjects().addGameObject(paddle);
    }

    private void createBricks(Vector2 windowDimensions) {
        for (int i = 0; i < this.columnsBricks; i++) {
            for (int j = 0; j < this.rowsBricks; j++) {
                Vector2 topLeftCorner = new Vector2(80 * i + 45, 25 * j + 45);
                GameObject brick = new Brick(topLeftCorner,
                        new Vector2(windowDimensions.x() / 10, 15),
                        this.gameHelper.imageReader.readImage(
                                "assets/brick.png", false),
                        BrickStrategyFactory.getStrategy(this.gameHelper,
                                this.counters,
                                windowDimensions, this,
                                0)
                );
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increment();
            }
        }
    }

    private void createWalls(Vector2 windowDimensions) {
        // WALLS
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(WALL_THICKNESS,
                        windowDimensions.y()), null));
        gameObjects().addGameObject(
                new GameObject(new Vector2(windowDimensions.x(), 0),
                        new Vector2(WALL_THICKNESS, windowDimensions.y()),
                        null));
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                        WALL_THICKNESS), null));

    }

    public void handleBall(Ball ball) {
        double ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            if (ball.getTag().equals("Normal Ball")) {
                lives.decrement();
                ball.setCenter(windowDimensions.mult(0.5f));
                hearts.update();
                numericCounter.update();
            } else this.deleteObject(ball);
        }
    }

    public boolean deleteObject(GameObject object) {
        return this.gameObjects().removeGameObject(object);
    }

    public boolean deleteObject(GameObject object, int layer) {
        return this.gameObjects().removeGameObject(object, layer);
    }

    public void addObject(GameObject object) {
        this.gameObjects().addGameObject(object);
    }

    public GameObjectCollection getGameObjects() {
        return this.gameObjects();
    }

}
