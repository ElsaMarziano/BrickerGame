package bricker.main;


import danogl.util.Counter;

public class Counters {
    public Counter lifeCounter;
    public Counter brickCounter;

    public Counters(Counter lifeCounter, Counter brickCounter) {
        this.lifeCounter = lifeCounter;
        this.brickCounter = brickCounter;
    }
}
