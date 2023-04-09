/*
 * Handles all the computation and back-end functioning
 * keeps a track of the grid for the maze along with the elements and their respective positions */

package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.PositionOccupiedException;
import model.exceptions.OutOfBoundsException;
import model.logging.Event;
import model.logging.EventLog;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Grid implements Writable {
    private final int gridSize;
    private final Map<Position, Element> grid;
    private final EventLog eventLog = EventLog.getInstance();
    private Cursor cursor;
    private Player player;
    private Monster monster;

    // Effects: creates an empty grid of the given size
    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new HashMap<>();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid.put(new Position(j, i), new Empty());
            }
        }

        player = new Player(new Position(0, 0), this);
        monster = new Monster(new Position(gridSize - 1, gridSize - 1), this);
        cursor = new Cursor(new Position(0, 0), this);

        replace(player.getPosition(), player);
        replace(monster.getPosition(), monster);
    }

    // Effects: creates a deep copy of the grid
    public Grid(Grid oldGrid) {
        this(oldGrid.gridSize);

        for (Entry<Position, Element> entry : oldGrid.grid.entrySet()) {
            Position key = entry.getKey();
            Element value = entry.getValue();

            if (value.getType().equals("Obstacle")) {
                this.grid.replace(key, new Obstacle());
            }
        }

        player = new Player(oldGrid.player);
        monster = new Monster(oldGrid.monster);
        cursor = new Cursor(oldGrid.cursor);

        try {
            player.setPosition(player.getPosition());
        } catch (PositionOccupiedException ignore) {
            // Player is in the same position
        } catch (OutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        try {
            monster.setPosition(monster.getPosition());
        } catch (PositionOccupiedException e) {
            // Monster is in the same position
        } catch (OutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    //Requires: gridJson must be a valid grid Json created using toJson()
    //Effects: translates the JSONObject into a Grid
    public Grid(JSONObject gridJson) {
        this(gridJson.getInt("gridSize"));

        JSONObject internalGridJson = gridJson.getJSONObject("internalGrid");
        Set<String> keys = internalGridJson.keySet();
        String cursorPos = gridJson.getString("cursorPos");
        this.cursor = new Cursor(new Position(cursorPos), this);

        for (String key : keys) {
            String type = internalGridJson.getString(key);
            Position position = new Position(key);
            switch (type) {
                case "Obstacle":
                    replace(position, new Obstacle());
                    break;
                case "Player":
                    Player player = new Player(position, this);
                    replace(position, player);
                    this.player = player;
                    break;
                case "Monster":
                    Monster monster = new Monster(position, this);
                    replace(position, monster);
                    this.monster = monster;
            }

        }
    }

    //Requires: Position p is within the bounds of the grid
    //Modifies: this
    //Effects: changes the position of the cursor
    public void setCursorPosition(Position p) throws OutOfBoundsException {
        cursor.setPosition(p);
        eventLog.logEvent(new Event("Placed cursor at: " + p));
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  changes the position of the player
    public void setPlayerPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        player.setPosition(p);
        eventLog.logEvent(new Event("Placed player at: " + p));
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  changes the position of the monster
    public void setMonsterPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        monster.setPosition(p);
        eventLog.logEvent(new Event("Placed monster at: " + p));
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects: places an Obstacle at the given Position
    public void placeObstacle(Position p) throws PositionOccupiedException, OutOfBoundsException {
        placeOnGrid(p, new Obstacle());
        eventLog.logEvent(new Event("Placed obstacle at: " + p));
    }

    //Modifies: this
    //Effects: moves the Cursor in the given direction if it's within bounds
    public void moveCursor(String dir) {
        cursor.move(dir);
        eventLog.logEvent(new Event("Cursor tried to move: " + dir));
    }

    //Modifies: this
    //Effects: - moves the Player in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Monster
    public void movePlayer(String dir) throws ContactException {
        player.move(dir);
        eventLog.logEvent(new Event("Player tried to move: " + dir));
    }

    //TODO: finish tests for Grid
    //Modifies: this
    //Effects: - moves the Monster in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Player
    public void moveMonster(String dir) throws ContactException {
        monster.move(dir);
        eventLog.logEvent(new Event("Monster tried to move: " + dir));
    }

    //Effects: returns the Cursor Position on the grid
    public Position getCursorPos() {
        return cursor.getPosition();
    }

    //Effects: returns the Player Position on the grid
    public Position getPlayerPos() {
        return player.getPosition();
    }

    //Effects: returns the Monster Position on the grid
    public Position getMonsterPos() {
        return monster.getPosition();
    }

    //Effects: returns the type of element at the given position
    public String getStatus(Position p) throws OutOfBoundsException {
        checkBounds(p);
        return grid.get(p).getType();
    }

    //Effects: returns the size of the grid
    public int getSize() {
        return gridSize;
    }

    //Requires: the Position is within bounds and empty
    //Modifies: this
    //Effects:  places the given Element at the given Position
    void placeOnGrid(Position p, Element e) throws OutOfBoundsException, PositionOccupiedException {
        checkEmpty(p);
        replace(p, e);
    }

    void replace(Position p, Element e) {
        grid.replace(p, e);
    }

    void checkContact(MovableElement currElement, Position newPos) throws ContactException {
        boolean contact = player.getPosition().equals(newPos);
        if (currElement instanceof Player) {
            contact =  monster.getPosition().equals(newPos);
        }
        if (contact) {
            throw new ContactException();
        }
    }

    //Requires: the Position should be within bounds
    //Effects:  throws error if the Position is occupied
    void checkEmpty(Position p) throws OutOfBoundsException, PositionOccupiedException {
        checkBounds(p);
        Element e = grid.get(p);
        if (!"Empty".equals(e.getType())) {
            throw new PositionOccupiedException();
        }
    }

    //Effects: throws an exception if Position is out of bounds
    void checkBounds(Position position) throws OutOfBoundsException {
        int x = position.getPosX();
        int y = position.getPosY();
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
            throw new OutOfBoundsException();
        }
    }

    //Effects: - returns the Position of the movable Element in the given direction
    //         - the Position doesn't need to be withing the grid
    Position getNewPosition(MovableElement movableObj, String dir) {
        Position oldPos = movableObj.getPosition();
        int newX = oldPos.getPosX();
        int newY = oldPos.getPosY();

        switch (dir.toLowerCase()) {
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gridSize", gridSize);
        json.put("cursorPos", getCursorPos().toString());

        JSONObject jsonGrid = new JSONObject();

        for (Entry<Position, Element> pairs : grid.entrySet()) {
            jsonGrid.put(pairs.getKey().toString(), pairs.getValue().toString());
        }
        json.put("internalGrid", jsonGrid);
        return json;
    }
}