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

        Position firstCell = new Position(0, 0);
        Position lastCell = new Position(gridSize - 1, gridSize - 1);

        player = new Player(firstCell, this);
        monster = new Monster(lastCell, this);
        cursor = new Cursor(firstCell, this);

        grid.replace(lastCell, monster);
        grid.replace(firstCell, player);
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

        Position firstCell = new Position(0, 0);
        Position lastCell = new Position(gridSize - 1, gridSize - 1);

        player = new Player(oldGrid.player);
        monster = new Monster(oldGrid.monster);
        cursor = new Cursor(oldGrid.cursor);

        //TODO: will delete obstacles in first and last cells
        swap(firstCell, player.getPosition());
        swap(lastCell, monster.getPosition());
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
                    grid.replace(position, new Obstacle());
                    break;
                case "Player":
                    this.player = new Player(position, this);
                    grid.replace(position, player);
                    break;
                case "Monster":
                    this.monster = new Monster(position, this);
                    grid.replace(position, monster);
            }

        }
    }

    //Requires: Position p is within the bounds of the grid
    //Modifies: this
    //Effects: changes the position of the cursor
    public void setCursorPosition(Position p) throws OutOfBoundsException {
        checkBounds(p);
        cursor.setPosition(p);
        log("Placed cursor at: " + p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  swaps the position of the player
    public void setPlayerPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        checkBounds(p);
        checkEmpty(p);
        swap(p, player.getPosition());
        player.setPosition(p);
        log("Placed player at: " + p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  swaps the position of the monster
    public void setMonsterPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        checkBounds(p);
        checkEmpty(p);
        swap(p, monster.getPosition());
        monster.setPosition(p);
        log("Placed monster at: " + p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects: places an Obstacle at the given Position
    public void placeObstacle(Position p) throws PositionOccupiedException, OutOfBoundsException {
        checkBounds(p);
        checkEmpty(p);
        grid.replace(p, new Obstacle());
        log("Placed obstacle at: " + p);
    }

    //Modifies: this
    //Effects: moves the Cursor in the given direction if it's within bounds
    public void moveCursor(String dir) {
        cursor.move(dir);
    }

    //Modifies: this
    //Effects: - moves the Player in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Monster
    public void movePlayer(String dir) throws ContactException {
        player.move(dir);
    }

    //TODO: finish tests for Grid
    //Modifies: this
    //Effects: - moves the Monster in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Player
    public void moveMonster(String dir) throws ContactException {
        monster.move(dir);
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

    //Effects: swaps the elements at the two positions
    void swap(Position p1, Position p2) {
        Element val1 = grid.get(p1);
        Element val2 = grid.get(p2);
        grid.replace(p1, val2);
        grid.replace(p2, val1);
    }

    //Effects: throws a contact exception if the newPos has a MovableElement
    void checkContact(Position newPos) throws ContactException {
        if (grid.get(newPos) instanceof MovableElement) {
            throw new ContactException();
        }
    }

    //Requires: the Position should be within bounds
    //Effects:  throws error if the Position is occupied
    void checkEmpty(Position p) throws PositionOccupiedException {
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

    //Effects: logs the event with description x
    private void log(String x) {
        EventLog eventLog = EventLog.getInstance();
        eventLog.logEvent(new Event(x));
    }
}