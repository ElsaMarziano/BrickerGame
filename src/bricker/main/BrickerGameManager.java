package bricker.main;

import bricker.brick_strategies.BrickStrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * This class describes the manager of a Bricker Game, where each brick can have a special
 * behavior
 *
 * @see BrickStrategyFactory
 */
public class BrickerGameManager extends GameManager {
    private static final int WALL_THICKNESS = 40;
    private static final int INITIAL_LIVES = 3;
    private static final String LOSE_MESSAGE = "You lose! Play again?";
    private static final String WIN_MESSAGE = "You win! Play again?";
    private static final int SPACE_BETWEEN_BRICKS = 5;
    private static final String GAME_NAME = "Bricker";
    private static final String BACKGROUND = "assets/DARK_BG2_small.jpeg";

    private static final int PADDING = 50;
    private static final Vector2 DEFAULT_WINDOW_SIZE = new Vector2(700, 500);
    private static final Vector2 NUMERIC_COUNTER_SIZE = new Vector2(30, 30);
    private static int rowsBricks = 7;
    private static int columnsBricks = 8;
    private final ArrayList<GameObject> puckBallsInGame;
    private final Counter brickCounter = new Counter(0);
    private final Counter lives = new Counter(INITIAL_LIVES);
    private final Counters counters = new Counters(lives, brickCounter);
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private GraphicLifeCounter hearts;
    private NumericLifeCounter numericCounter;
    private GameHelper gameHelper;
    private UserInputListener inputListener;

    /**
     * Construct a new Bricker Game Manager instance
     *
     * @param windowTitle title of the window
     * @param windowSize  size of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowSize) {
        super(windowTitle, windowSize);
        puckBallsInGame = new ArrayList<>();
    }

    /**
     * Get number of bricks and rows from user if needed, start game
     *
     * @param args rows and bricks (user)
     */
    public static void main(String[] args) {
        Vector2 windowSize = DEFAULT_WINDOW_SIZE;
        if (args.length == 2) {
            rowsBricks = Integer.parseInt(args[0]);
            columnsBricks = Integer.parseInt(args[1]);
        }
        new BrickerGameManager(GAME_NAME, windowSize).run();
    }

    /**
     * Overrides the update function of GameManager. Handles win and loss conditions,
     * update lives when needed,handle balls position
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.gameObjects();
        Iterator<GameObject> iterator = puckBallsInGame.iterator();
        while (iterator.hasNext()) { //Check all puckBalls if there are in the screen
            GameObject obj = iterator.next();
            if (obj.getCenter().y() > windowDimensions.y()) {
                iterator.remove();
                this.deleteObject(obj);
            }
        }
        handleBall(ball);
        String prompt = "";
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = WIN_MESSAGE;
        } else if (lives.value() == 0) {
            prompt = LOSE_MESSAGE;
        } else if (brickCounter.value() == 0) {
            prompt = WIN_MESSAGE;
        }
        if (!prompt.isEmpty() && windowController.openYesNoDialog(prompt)) {
            lives.reset();
            lives.increaseBy(INITIAL_LIVES);
            windowController.resetGame();
        } else if (!prompt.isEmpty()) {
            windowController.closeWindow();
        }
    }

    /**
     * This function initialize a game of Bricker
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.inputListener = inputListener;
        this.gameHelper = new GameHelper(imageReader, soundReader, inputListener);
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(),
                imageReader.readImage(BACKGROUND, false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        createWalls();
        createBricks();
        createLifeCounters(imageReader);
        // CREATING BALL
        ball = new Ball(Vector2.ZERO, Ball.DEFAULT_SIZE,
                imageReader.readImage(Ball.BALL_IMAGE,
                        true),
                soundReader.readSound(Ball.BLOP_SOUND));
        // Add ball object to game
        Random rand = new Random();
        int velX = 1, velY = 1;
        if (rand.nextBoolean()) velX = -1;
        if (rand.nextBoolean()) velY = -1;
        ball.setVelocity(new Vector2(velX, velY).mult(100));
        ball.setCenter(windowDimensions.mult(0.5f));
        this.addObject(ball);
        // CREATING PADDLE
        Renderable paddleImage = imageReader.readImage(Paddle.PADDLE_IMAGE,
                true);
        GameObject paddle = new Paddle(Vector2.ZERO, Paddle.DEFAULT_SIZE,
                paddleImage, inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDING));
        this.gameObjects().addGameObject(paddle);
    }


    /* This function creates bricks according to the window dimensions and the number of rows and
     columns.
     * It calls the Brick constructor and the BrickStrategyFactory to assign a Strategy to each
      brick
     * */
    private void createBricks() {
        int brickHeight = 15;
        float brickLength = (windowDimensions.x() -
                (WALL_THICKNESS + SPACE_BETWEEN_BRICKS) * 2) / columnsBricks;
        Vector2 brickSize = new Vector2(brickLength, brickHeight);
        Vector2 baseBrickLocation = new Vector2(WALL_THICKNESS + SPACE_BETWEEN_BRICKS,
                WALL_THICKNESS + SPACE_BETWEEN_BRICKS);
        for (int i = 0; i < rowsBricks; i++) {
            for (int j = 0; j < columnsBricks; j++) {
                Vector2 topLeftCorner = baseBrickLocation.add(new Vector2(i *
                        (brickLength + SPACE_BETWEEN_BRICKS), j * (brickHeight +
                        SPACE_BETWEEN_BRICKS)));
                Brick brick = new Brick(topLeftCorner, brickSize,
                        this.gameHelper.imageReader.readImage(
                                Brick.BRICK_IMAGE, false),
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

    /* This function creates two walls on the sides and an upper wall.
     */
    private void createWalls() {
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

    private void createLifeCounters(ImageReader imageReader) {
        hearts = new GraphicLifeCounter(new Vector2(PADDING,
                windowDimensions.y() - PADDING), Heart.DEFAULT_SIZE,
                lives, imageReader.readImage(Heart.HEART_IMAGE,
                true),
                this, lives.value());
        numericCounter = new NumericLifeCounter(lives,
                new Vector2(windowDimensions.x() - PADDING,
                        windowDimensions.y() - PADDING), NUMERIC_COUNTER_SIZE,
                this);
        this.gameObjects().addGameObject(hearts, Layer.BACKGROUND);
        this.gameObjects().addGameObject(numericCounter, Layer.BACKGROUND);
    }

    /*
This function handles the behavior of the main ball when it exits the screen:
 update lives and return it to the center.
*/
    private void handleBall(Ball ball) {
        double ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            lives.decrement();
            ball.setCenter(windowDimensions.mult(0.5f));
            Random rand = new Random();
            int velX = 1, velY = 1;
            if (rand.nextBoolean()) velX = -1;
            if (rand.nextBoolean()) velY = -1;
            ball.setVelocity(new Vector2(velX, velY).mult(100));
            hearts.update();
            numericCounter.update();
        }
    }


    /**
     * This function removes an object from the gameObjects container
     *
     * @param object the object to remove
     * @return true if removal was successful, else false
     */
    public boolean deleteObject(GameObject object) {
        return this.gameObjects().removeGameObject(object);
    }

    /**
     * This function removes an object from the gameObjects container
     *
     * @param object the object to remove
     * @param layer  the layer to remove the object from
     * @return true if object was successfully deleted, else false
     */
    public boolean deleteObject(GameObject object, int layer) {
        return this.gameObjects().removeGameObject(object, layer);
    }

    /**
     * This function adds an object to the gameObjects container
     *
     * @param object the object to add
     */
    public void addObject(GameObject object) {
        this.gameObjects().addGameObject(object);
        if (object.getTag().equals(Ball.PUCK_BALL_TAG)) {
            puckBallsInGame.add(object);
        }
    }

    /**
     * This function adds an object to the right layer of the gameObjects container
     *
     * @param object the object to remove
     * @param layer  the layer to remove the object from
     */

    public void addObject(GameObject object, int layer) {
        this.gameObjects().addGameObject(object, layer);
    }

    /**
     * This function return the Ball object
     *
     * @return Ball
     */
    public Ball getBall() {
        return ball;
    }
}
