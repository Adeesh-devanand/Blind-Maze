/*

 */

package model;

public class Maze {
    private String name;
    private Grid grid;

    //EFFECTS: Creates blank Maze with a given name
    public Maze(String name) {
        this.name = name;
        grid = new Grid(10, 10);
    }

    //MODIFIES: this
    //EFFECTS: Starts time from 0
    public void startTime() {
    }

    //EFFECTS: Return Time Elapsed
    public MazeTimer getTime() {
        return null;
    }

    //MODIFIES: this
    //EFFECTS: Pause Time
    public void pauseTime() {
    }

    public String getName() {
        return name;
    }

    //REQUIRES: dir should be one of "left", "right", "up", "down"
    //MODIFIES: this
    //EFFECTS: Moves Player
    public void movePlayer(String dir) {
        grid.movePlayer(dir);
    }

    //MODIFIES: this
    //EFFECTS: Returns Entire Grid
    public Grid returnGrid() {
        return null;
    }

    //EFFECTS: Returns 5x5 Grid around Player
    public Grid returnPlayerGrid() {
        return null;
    }

    //EFFECTS: Returns Position of Player
    public Position returnPlayerPosition() {
        return grid.getPlayerPos();
    }

    //EFFECTS: Returns Position of Monster
    public Position returnMonsterPosition() {
        return grid.getMonsterPos();
    }

    //MODIFIES: this
    //EFFECTS: End Maze Game(Reached Exit)
    public void endGame() {
    }

    //MODIFIES: this
    //EFFECTS: Exit Maze
    public void exitMaze() {
    }
}
