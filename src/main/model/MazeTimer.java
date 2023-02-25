package model;

import java.util.Timer;

public class MazeTimer {
    private Timer timer;

    //EFFECTS: Creates a timer counting from 0
    public MazeTimer() {
        timer = new Timer();
    }

    //MODIFIES: this
    //EFFECTS: Pauses timer
    public void pauseTimer() {}
}
