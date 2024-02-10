package bricker.main;


import danogl.util.Counter;

/**
 * This class contains every counter needed for the BrickerGameManager, and is mainly intended for
 * clarity
 */
public class Counters {
    /**
     * The number of lives left
     */
    public Counter lifeCounter;
    /**
     * The number of bricks left
     */
    public Counter brickCounter;

    /**
     * Initialize a Counters instance
     *
     * @param lifeCounter  counter of lives remaining
     * @param brickCounter counter of bricks remaining
     */
    public Counters(Counter lifeCounter, Counter brickCounter) {
        this.lifeCounter = lifeCounter;
        this.brickCounter = brickCounter;
    }
}
