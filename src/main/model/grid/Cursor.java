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
    public void setPosition(Position p) throws OutOfBoundsException {
        grid.checkBounds(p);
        this.position = p;
    }

    @Override
    public void move(String dir) {
        Position newPos = grid.getNewPosition(this, dir);
        try {
            setPosition(newPos);
        } catch (OutOfBoundsException ignored) {
            //simply don't move
        }
    }
}
