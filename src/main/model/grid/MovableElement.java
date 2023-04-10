package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.OutOfBoundsException;
import model.exceptions.PositionOccupiedException;
import model.logging.Event;
import model.logging.EventLog;

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

    //Modifies: this, grid
    //Effects: moves the entity to that position and clears the old position
    public void setPosition(Position p) {
        this.position = p;
    }

    //Modifies: this, grid
    //Effects: moves the entity to that position if within bounds and empty
    public void move(String dir) throws ContactException {
        try {
            Position newPos = Position.getNewPosition(getPosition(), dir);
            grid.checkBounds(newPos);
            grid.checkContact(newPos);
            grid.checkEmpty(newPos);
            grid.swap(getPosition(), newPos);
            setPosition(newPos);
            logMovement(dir);
        } catch (OutOfBoundsException | PositionOccupiedException ignored) {
            //simply don't move
        }
    }

    protected void logMovement(String dir) {
        EventLog eventLog = EventLog.getInstance();
        eventLog.logEvent(new Event(this.getType() + " moved: " + dir));
    }

    public Position getPosition() {
        return position;
    }

}
