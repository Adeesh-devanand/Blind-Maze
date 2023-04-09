package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.OutOfBoundsException;
import model.exceptions.PositionOccupiedException;

public abstract class MovableElement extends Element {
    protected Position position;
    protected Grid grid;

    public MovableElement(Position position, String type, Grid grid) {
        super(type);
        this.position = position;
        this.grid = grid;
    }

    public MovableElement(MovableElement element) {
        super(element);
        this.position = element.position;
        this.grid = element.grid;
    }

    public void setPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        Position oldPos = getPosition();
        grid.placeOnGrid(p, this);
        grid.replace(oldPos, new Empty());
        this.position = p;
    }

    public void move(String dir) throws ContactException {
        Position newPos = grid.getNewPosition(this, dir);
        grid.checkContact(this, newPos);
        try {
            setPosition(newPos);
        } catch (OutOfBoundsException | PositionOccupiedException ignored) {
            //simply don't move
        }
    }

    public Position getPosition() {
        return position;
    }

}
