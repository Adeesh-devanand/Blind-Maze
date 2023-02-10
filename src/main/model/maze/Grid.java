/*

 */

package model.maze;

public class Grid {
    //EFFECTS: Creates an Empty Grid
    public Grid() {}

    //MODIFIES: this
    //EFFECTS: Places Obstacle on Grid
    public void placeObstacle() {}

    //REQUIRES: should be a valid move
    //MODIFIES: this
    //EFFECTS: Moves Player on Grid
    public void movePlayer() {}

    //REQUIRES: should be a valid move
    //MODIFIES: this
    //EFFECTS: Moves Monster on Grid
    public void moveMonster(Position p) {}

    //EFFECTS: returns Position of Player
    public Position getPlayerPos() {
        return null;
    }

    //EFFECTS: Gives Position of Monster
    public Position getMonsterPos() {
        return null;
    }

    //EFFECTS: Gives Back Grid with Obstacles
    public Grid getGrid() {
        return this;
    }
}