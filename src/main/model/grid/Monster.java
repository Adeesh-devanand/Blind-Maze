/*
 * Represents a Monster on the grid */

package model.grid;

import model.Position;

public class Monster extends MovableElement {

    public Monster(Position position, Grid grid) {
        super(position, "Monster", grid);
    }

    public Monster(Monster monster) {
        super(monster);
    }
}
