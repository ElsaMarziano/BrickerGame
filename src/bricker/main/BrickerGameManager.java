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
import java.util.Random;

/**
 * This class describes the manager of a Bricker Game, where each brick can have a special behavior
 *
 * @see BrickStrategyFactory
 */
public class BrickerGameManager extends GameManager {
    private static final int WALL_THICKNESS = 40;
    private static final int INITIAL_LIVES = 3;
    private static final String LOSE_MESSAGE = "You lose! Play again?";
    private static final String WIN_MESSAGE = "You win! Play again?";
    private static final String INCORRECT_NUM_OF_ARGS =
            "Invalid input, try again with 2 parameters or 0";
    private static final int SPACE_BETWEEN_BRICKS = 5;
    public static int rowsBricks = 7;
    public static int columnsBricks = 8;

    public Counter brickCounter = new Counter(0);
    public Counter lives = new Counter(INITIAL_LIVES);
    private final Counters counters = new Counters(lives, brickCounter);
    public Ball ball;

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
    }

    /**
     * Get number of bricks and rows from user if needed, start game
     *
     * @param args rows and bricks (user)
     */
    public static void main(String[] args) {
        Vector2 windowSize = new Vector2(700, 500);
        if (args.length == 2) {
            rowsBricks = Integer.parseInt(args[0]);
            columnsBricks = Integer.parseInt(args[1]);
        } else if (args.length != 0) { //num of args is not 2 and not 0
            System.err.println(INCORRECT_NUM_OF_ARGS);
        }
        new BrickerGameManager("Bricker", windowSize).run();
    }

    /**
     * Overrides the update function of GameManager. Handles win and loss conditions, update lives when needed,
     * handle balls position
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
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        // Initialize game, save everything in fields
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        this.inputListener = inputListener;
        this.gameHelper = new GameHelper(imageReader, soundReader, inputListener);
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        // Create background, walls, bricks and lives
        GameObject background = new GameObject(
                Vector2.ZERO, windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg",
                        false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // Later purposes
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        createWalls(windowDimensions);
        createBricks(windowDimensions);
        hearts = new GraphicLifeCounter(new Vector2(60, windowDimensions.y() - 10), new Vector2(30, 30),
                lives, imageReader.readImage("assets/heart.png", true),
                this, lives.value());
        numericCounter = new NumericLifeCounter(lives,
                new Vector2(windowDimensions.x() - 50, windowDimensions.y() - 50), new Vector2(30, 30),
                this);
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

    /* This function creates bricks according to the window dimensions and the number of rows and columns.
     * It calls the Brick constructor and the BrickStrategyFactory to assign a Strategy to each brick
     * */
    private void createBricks(Vector2 windowDimensions) {
        // Calculate brick length according to number of bricks and size of window
        float brickLength = (windowDimensions.x() - (WALL_THICKNESS + SPACE_BETWEEN_BRICKS) * 2) / columnsBricks;
        Vector2 brickSize = new Vector2(brickLength, 15);
        Vector2 baseBrickLocation = new Vector2(WALL_THICKNESS + SPACE_BETWEEN_BRICKS, WALL_THICKNESS + SPACE_BETWEEN_BRICKS);
        // Create eahc row and oclumn of bricks
        for (int i = 0; i < rowsBricks; i++) {
            for (int j = 0; j < columnsBricks; j++) {
                // Calculate position of new brick according to index and base position
                Vector2 topLeftCorner = baseBrickLocation.add(new Vector2(i * (brickLength + SPACE_BETWEEN_BRICKS), j * (15 + SPACE_BETWEEN_BRICKS)));
                Brick brick = new Brick(topLeftCorner, brickSize,
                        this.gameHelper.imageReader.readImage(
                                "assets/brick.png", false),
                        BrickStrategyFactory.getStrategy(this.gameHelper,
                                this.counters,
                                windowDimensions, this,
                                0)
                );
                // Add brick to gameObjects
                this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increment();
            }
        }
    }

    /* This function creates two walls on the sides and an upper wall.
     * */
    private void createWalls(Vector2 windowDimensions) {
        // Left wall
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(WALL_THICKNESS,
                        windowDimensions.y()), null));
        // Right wall
        gameObjects().addGameObject(
                new GameObject(new Vector2(windowDimensions.x(), 0),
                        new Vector2(WALL_THICKNESS, windowDimensions.y()),
                        null));
        // Upper wall
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                        WALL_THICKNESS), null));

    }

    /*
    This function handles the behavior of all the balls when they exit the screen.
    For the normal ball, update lives and return it to the center.
    For puck balls, delete it from the game objects
    */
    public void handleBall(Ball ball) {
        double ballHeight = ball.getCenter().y();
        // If ball got out of screen
        if (ballHeight > windowDimensions.y()) {
            // Normal ball behavior
            if (ball.getTag().equals("Normal Ball")) {
                lives.decrement();
                // Return to center
                ball.setCenter(windowDimensions.mult(0.5f));
                Random rand = new Random();
                int velX = 1, velY = 1;
                if (rand.nextBoolean()) velX = -1;
                if (rand.nextBoolean()) velY = -1;
                // Give new direction
                ball.setVelocity(new Vector2(velX, velY).mult(100));
                // Update lives
                hearts.update();
                numericCounter.update();
            } else this.deleteObject(ball); // For puck balls - delete them from gameObjects
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
     * This function return the gameObjects present in the game
     *
     * @return this.gameObjects
     */
    public GameObjectCollection getGameObjects() {
        return this.gameObjects();
    }

}
