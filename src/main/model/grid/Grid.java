/*
 * Handles all the computation and back-end functioning
 * keeps a track of the grid for the maze along with the elements and their respective positions */

package model.grid;

import model.Position;
import model.exceptions.ContactException;
import model.exceptions.PositionOccupiedException;
import model.exceptions.OutOfBoundsException;
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

        player = new Player(new Position(0, 0));
        monster = new Monster(new Position(gridSize - 1, gridSize - 1));
        cursor = new Cursor(new Position(0, 0));

        grid.replace(player.getPosition(), player);
        grid.replace(monster.getPosition(), monster);
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
            setPlayerPosition(player.getPosition());
            setMonsterPosition(monster.getPosition());
        } catch (PositionOccupiedException ignore) {
            // Element is in the same position
        }
    }

    //Requires: gridJson must be a valid grid Json created using toJson()
    //Effects: translates the JSONObject into a Grid
    public Grid(JSONObject gridJson) {
        this(gridJson.getInt("gridSize"));

        JSONObject internalGridJson = gridJson.getJSONObject("internalGrid");
        Set<String> keys = internalGridJson.keySet();
        String cursorPos = gridJson.getString("cursorPos");
        this.cursor = new Cursor(new Position(cursorPos));

        for (String key : keys) {
            String type = internalGridJson.getString(key);
            Position position = new Position(key);
            switch (type) {
                case "Obstacle":
                    grid.replace(position, new Obstacle());
                    break;
                case "Player":
                    Player player = new Player(position);
                    grid.replace(position, player);
                    this.player = player;
                    break;
                case "Monster":
                    Monster monster = new Monster(position);
                    grid.replace(position, monster);
                    this.monster = monster;
            }
        }
    }

    //Requires: Position p is within the bounds of the grid
    //Modifies: this
    //Effects: changes the position of the cursor
    public void setCursorPosition(Position p) throws OutOfBoundsException {
        if (isOutOfBounds(p)) {
            throw new OutOfBoundsException();
        }
        cursor.setPosition(p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  changes the position of the player
    public void setPlayerPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        Position oldPos = player.getPosition();
        placeOnGrid(p, player);
        grid.replace(oldPos, new Empty());
        player.setPosition(p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects:  changes the position of the monster
    public void setMonsterPosition(Position p) throws PositionOccupiedException, OutOfBoundsException {
        Position oldPos = monster.getPosition();
        placeOnGrid(p, monster);
        grid.replace(oldPos, new Empty());
        monster.setPosition(p);
    }

    //Requires: Position p is within the bounds of the grid, and should be empty
    //Modifies: this
    //Effects: places an Obstacle at the given Position
    public void placeObstacle(Position p) throws PositionOccupiedException, OutOfBoundsException {
        placeOnGrid(p, new Obstacle());
    }

    //Modifies: this
    //Effects: moves the Cursor in the given direction if it's within bounds
    public void moveCursor(String dir) {
        Position newPos = getNewPosition(cursor, dir);
        try {
            setCursorPosition(newPos);
        } catch (OutOfBoundsException e) {
            //simply don't move
        }
    }

    //Modifies: this
    //Effects: - moves the Player in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Monster
    public void movePlayer(String dir) throws ContactException {
        Position newPos = getNewPosition(player, dir);
        if (newPos.equals(getMonsterPos())) {
            throw new ContactException();
        }
        try {
            setPlayerPosition(newPos);
        } catch (OutOfBoundsException | PositionOccupiedException ignored) {
            //simply don't move
        }
    }

    //TODO: finish tests for Grid
    //Modifies: this
    //Effects: - moves the Monster in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Player
    public void moveMonster(String dir) throws ContactException {}

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
    public String getStatus(Position position) throws OutOfBoundsException {
        if (isOutOfBounds(position)) {
            throw new OutOfBoundsException();
        }
        return grid.get(position).getType();
    }

    //Effects: returns the size of the grid
    public int getSize() {
        return gridSize;
    }

    //Requires: the Position is within bounds and empty
    //Modifies: this
    //Effects:  places the given Element at the given Position
    private void placeOnGrid(Position p, Element e) throws OutOfBoundsException, PositionOccupiedException {
        if (isCellOccupied(p)) {
            throw new PositionOccupiedException();
        }
        grid.replace(p, e);
    }

    //Requires: the Position should be within bounds
    //Effects:  returns if the Position is occupied
    private boolean isCellOccupied(Position p) throws OutOfBoundsException {
        if (isOutOfBounds(p)) {
            throw new OutOfBoundsException();
        }
        Element e = grid.get(p);
        return !"Empty".equals(e.getType());
    }

    //Effects: returns if the Position is out of bounds
    private boolean isOutOfBounds(Position position) {
        int x = position.getPosX();
        int y = position.getPosY();
        return x < 0 || y < 0 || x >= gridSize || y >= gridSize;
    }

    //Effects: - returns the Position of the movable Element in the given direction
    //         - the Position doesn't need to be withing the grid
    private Position getNewPosition(Element movableObj, String dir) {
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