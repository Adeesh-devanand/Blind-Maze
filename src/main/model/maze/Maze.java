/*

 */

package model.maze;

public class Maze {
    //EFFECTS: Creates blank Maze
    public Maze(String name) {
        new Grid();
    }

    //MODIFIES: this
    //EFFECTS: Starts time from 0
    public void startTime() {}

    //EFFECTS: Return Time Elapsed
    public Timer getTime() {
        return null;
    }

    //MODIFIES: this
    //EFFECTS: Pause Time
    public void pauseTime() {}

    //MODIFIES: this
    //EFFECTS: Moves Player
    public void movePlayer() {}

    //MODIFIES: this
    //EFFECTS: Returns Entire Grid
    public Grid returnGrid() {
        return null;
    }

    //EFFECTS: Returns Grid around Player
    public Grid returnPlayerGrid() {
        return null;
    }

    //EFFECTS: Returns Position of Player
    public Position returnPlayerPosition() {
        return null;
    }

    //EFFECTS: Returns Position of Monster
    public Position returnMonsterPosition() {
        return null;
    }

    //MODIFIES: this
    //EFFECTS: End Maze Game(Reached Exit)
    public void endGame() {}

    //MODIFIES: this
    //EFFECTS: Exit Maze
    public void exitMaze() {}
}
