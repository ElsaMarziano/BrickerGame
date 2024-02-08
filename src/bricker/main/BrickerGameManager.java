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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private GraphicLifeCounter hearts;
    private NumericLifeCounter numericCounter;
    private GameHelper gameHelper;
    private UserInputListener inputListener;
    private final ArrayList<GameObject> puckBallsInGame;

    public BrickerGameManager(String windowTitle, Vector2 windowSize) {
        super(windowTitle, windowSize);
        puckBallsInGame = new ArrayList<>();
    }

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

    public Ball getBall(){
        return ball;
    }
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

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.inputListener = inputListener;
        this.gameHelper = new GameHelper(imageReader, soundReader, inputListener);
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        GameObject background = new GameObject(
                Vector2.ZERO, windowController.getWindowDimensions(),
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
                soundReader.readSound("assets/Bubble5_4.wav"));
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
        float brickLength = (windowDimensions.x() - (WALL_THICKNESS + SPACE_BETWEEN_BRICKS) * 2) / columnsBricks;
        Vector2 brickSize = new Vector2(brickLength, 15);
        Vector2 baseBrickLocation = new Vector2(WALL_THICKNESS + SPACE_BETWEEN_BRICKS, WALL_THICKNESS + SPACE_BETWEEN_BRICKS);
        for (int i = 0; i < rowsBricks; i++) {
            for (int j = 0; j < columnsBricks; j++) {
                Vector2 topLeftCorner = baseBrickLocation.add(new Vector2(i * (brickLength + SPACE_BETWEEN_BRICKS), j * (15 + SPACE_BETWEEN_BRICKS)));
                Brick brick = new Brick(topLeftCorner, brickSize,
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

    private void handleBall(GameObject ball) {
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

    public boolean deleteObject(GameObject object) {
        return this.gameObjects().removeGameObject(object);
    }

    public boolean deleteObject(GameObject object, int layer) {
        return this.gameObjects().removeGameObject(object, layer);
    }

    public void addObject(GameObject object) {
        this.gameObjects().addGameObject(object);
        if (object.getTag().equals("Puck Ball")){
            puckBallsInGame.add(object);
        }
    }

    public GameObjectCollection getGameObjects() {
        return this.gameObjects();
    }

}
