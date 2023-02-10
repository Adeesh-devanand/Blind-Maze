/*

 */

package model;

import model.maze.Maze;

public class Menu {
    //REQUIRES: name is not already in list of mazes
    //MODIFIES: this
    //EFFECTS: Adds an empty maze to list of available mazes and returns it
    public Maze createMaze(String name) {
        Maze maze = new Maze(name);
        return null;
    }

    //MODIFIES: this
    //EFFECTS: Toggles state between Play and Toggle
    public void toggleMode() {}

    //REQUIRES: Maze name is in the list of Mazes
    //EFFECTS: Return the selected Maze
    public Maze returnMaze(String name) {
        return null;
    }

    //EFFECTS: Returns the list of available mazes
    public void returnLoMazes() {}

    //MODIFIES: this
    //EFFECTS: Kills the program
    public void killGame() {}
}
