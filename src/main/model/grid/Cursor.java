/*
 * Represents the cursor which can be moved around in EditMode */

package model.grid;

import model.Position;
import model.exceptions.OutOfBoundsException;

public class Cursor extends MovableElement {

    public Cursor(Position position, Grid grid) {
        super(position, "cursor", grid);
    }

    public Cursor(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void setPosition(Position p) {
        this.position = p;
    }

    @Override
    public void move(String dir) {
        try {
            Position newPos = Position.getNewPosition(getPosition(), dir);
            grid.checkBounds(newPos);
            setPosition(newPos);
            logMovement(dir);
        } catch (OutOfBoundsException ignored) {
            //simply don't move
        }
    }
}
