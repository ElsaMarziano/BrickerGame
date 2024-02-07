package bricker.main;


import danogl.util.Counter;

/**
 * This class contains every counter needed for the BrickerGameManager, and is mainly intended for clarity
 */
public class Counters {
    public Counter lifeCounter;
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
