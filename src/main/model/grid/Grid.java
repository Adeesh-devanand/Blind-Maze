/*
* Handles all the computation and back-end functioning
* keeps a track of the grid for the maze along with the elements and their respective positions
*
* NOTE: All positions start from the top left corner i.e top left is (0, 0)
* 'y' represents the y axes, and 'x' represents the x axes */

package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;

import java.util.HashMap;
import java.util.Map;

public class Grid {
    private final int gridSize;
    private final Map<Position, Element> grid;

    private Player player;
    private Monster monster;
    private Cursor cursor;


    //EFFECTS: Creates an Empty Grid of size (x across by y down)
    public Grid(int gridSize) {
        this.gridSize = gridSize;
        grid = new HashMap<Position, Element>();

        Position temp;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                temp = new Position(j, i);
                grid.put(temp, new Empty());
            }
        }

        player = new Player(new Position(0, 0));
        monster = new Monster(new Position(gridSize - 1, gridSize - 1));
        cursor = new Cursor(new Position(0, 0));

        grid.replace(player.getPosition(), player);
        grid.replace(monster.getPosition(), monster);
    }

    //EFFECTS: Creates a copy of the given Grid object
    public Grid(Grid oldGrid) {
        this.gridSize = oldGrid.gridSize; //you can access
        this.grid = new HashMap<>();

        for (Map.Entry<Position, Element> entry : oldGrid.grid.entrySet()) {
            Position key = entry.getKey();
            Element value = entry.getValue();

            switch (value.getType()) {
                case "Empty":
                    this.grid.put(key, new Empty());
                    break;
                case "Obstacle":
                    this.grid.put(key, new Obstacle());
                    break;
                default:
            }
        }

        this.player = new Player(oldGrid.player);
        this.monster = new Monster(oldGrid.monster);
        this.cursor = new Cursor(oldGrid.cursor);

        grid.put(this.player.getPosition(), this.player);
        grid.put(this.monster.getPosition(), this.monster);
        grid.put(this.cursor.getPosition(), this.cursor);
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Obstacle on grid at given position
    public void placeObstacle(Position position) throws ElementAlreadyExistsException, OutOfBoundsException {
        if (!isWithinBounds(position)) {
            throw new OutOfBoundsException();
        }
        if (!isCellEmpty(position)) {
            throw new ElementAlreadyExistsException();
        }
        grid.replace(position, new Obstacle());
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Player on grid at given position
    public void setPlayerPosition(Position position) throws ElementAlreadyExistsException, OutOfBoundsException {
        if (!isWithinBounds(position)) {
            throw new OutOfBoundsException();
        }
        if (!isCellEmpty(position)) {
            throw new ElementAlreadyExistsException();
        }
        clearCell(player.getPosition());
        player.setPosition(position);
        grid.replace(position, player);
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Monster on grid at given position
    public void setMonsterPosition(Position position) throws ElementAlreadyExistsException, OutOfBoundsException {
        if (!isCellEmpty(position)) {
            throw new ElementAlreadyExistsException();
        }
        if (!isWithinBounds(position)) {
            throw new OutOfBoundsException();
        }

        assert grid.containsKey(monster.getPosition());
        clearCell(monster.getPosition());
        monster.setPosition(position);
        grid.replace(position, monster);
    }

    public void setCursorPosition(Position position) throws OutOfBoundsException {
        if (!isWithinBounds(position)) {
            throw new OutOfBoundsException();
        }
        cursor.setPosition(position);
    }

    //TODO: resolve conflict when monster and player collide
    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid setPosition, else doesn't do anything
    public void movePlayer(String dir) throws ContactException {
        Position newPos = getNextPos(player, dir);
        if (newPos.equals(getMonsterPos())) {
            throw new ContactException();
        }
        try {
            setPlayerPosition(newPos);
        } catch (OutOfBoundsException | ElementAlreadyExistsException ignored) {
            ;
        }
    }

    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid setPosition, else doesn't do anything
    public void moveMonster(String dir) throws ContactException {
        Position newPos = getNextPos(monster, dir);
        if (newPos.equals(getPlayerPos())) {
            throw new ContactException();
        }
        try {
            setMonsterPosition(newPos);
        } catch (OutOfBoundsException | ElementAlreadyExistsException ignored) {
            ;
        }
    }

    public void moveCursor(String dir) {
        Position newPos = getNextPos(cursor, dir);
        try {
            setCursorPosition(newPos);
        } catch (OutOfBoundsException ignored) {
            ;
        }
    }

    //EFFECTS: returns Position of Player
    public Position getPlayerPos() {
        return player.getPosition();
    }

    //EFFECTS: returns Position of Monster
    public Position getMonsterPos() {
        return monster.getPosition();
    }

    public Position getCursorPos() {
        return cursor.getPosition();
    }

    private Position getNextPos(Element movableObj, String dir) {
        Position oldPos = movableObj.getPosition();
        int newX = oldPos.getPosX();
        int newY = oldPos.getPosY();

        switch (dir) {
            case "r":
                newX++;
                break;
            case "l":
                newX--;
                break;
            case "u":
                newY--;
                break;
            case "d":
                newY++;
        }
        return new Position(newX, newY);
    }

    //REQUIRES: Position is withing grid limits
    //EFFECTS: returns the status at given grid Position
    //String is one of "o", "p", "m", "e"//
    public String getStatus(Position position) throws OutOfBoundsException {
        if (!isWithinBounds(position)) {
            throw new OutOfBoundsException();
        }
        Element e = grid.get(position);
        return e.getType();
    }

    //REQUIRES: p shouldn't be the position of a player or monster
    //MODIFIES: this
    //EFFECTS: Clears the Position on the grid
    private void clearCell(Position position) {
        grid.replace(position, new Empty());
    }

    //EFFECTS: returns if the given position is empty or not
    public boolean isCellEmpty(Position position) {
        Element e = grid.get(position);
        return "Empty".equals(e.getType());
    }

    //EFFECTS: returns if the given Position is within grid limits
    private boolean isWithinBounds(Position position) {
        int x = position.getPosY();
        int y = position.getPosY();
        return x >= 0 && y >= 0 && x < gridSize && y < gridSize;
    }

    public int getSize() {
        return gridSize;
    }
}