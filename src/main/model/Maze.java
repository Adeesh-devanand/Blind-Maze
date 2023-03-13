/*
 * A typical controller, handles all interaction between UI, and the model
 * the UI is only dependent on this class in the model package, all instructions
 * to the model are fed through this class */

package model;

import model.grid.Grid;
import model.exceptions.ContactException;
import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

public class Maze implements Writable {
    private final String name;
    private final Grid grid;

    //EFFECTS: Creates a blank Maze with a given name
    public Maze(String name) {
        this(name, 10);
    }

    //EFFECTS: Creates a blank Maze with a given name and size
    public Maze(String name, int gridSize) {
        this.name = name;
        grid = new Grid(gridSize);
    }

    //EFFECTS: Creates a deep copy of the given Maze
    public Maze(Maze oldMaze) {
        this.name = oldMaze.name; //you can access
        this.grid = new Grid(oldMaze.grid);//you can access
    }

    public Maze(String name, JSONObject grid) {
        this.name = name;
        this.grid = parseGrid(grid);
    }

    private Grid parseGrid(JSONObject gridJson) {
        int gridSize = gridJson.getInt("gridSize");
        String cursorPos = gridJson.getString("cursorPos");
        JSONObject internalGridJson = gridJson.getJSONObject("internalGrid");
        Grid grid = new Grid(gridSize, cursorPos, internalGridJson);
        return grid;
    }

    //MODIFIES: this
    //EFFECTS: - moves player on the grid if it is a valid setPosition,
    //         - returns whether the player made contact with the monster
    public boolean movePlayer(String dir) {
        try {
            grid.movePlayer(dir);
        } catch (ContactException e) {
            return true;
        }
        return false;
    }

    public boolean moveCursor(String dir) {
        grid.moveCursor(dir);
        return true;
    }

    //REQUIRES: y, and x should be in the grid limit
    //MODIFIES: this
    //EFFECTS: places an entity on the grid
    public void placeEntity(String entity) throws ElementAlreadyExistsException, OutOfBoundsException {
        Position p = grid.getCursorPos();
        switch (entity) {
            case "Player":
                grid.setPlayerPosition(p);
                break;
            case "Obstacle":
                grid.placeObstacle(p);
                break;
            case "Monster":
                grid.setMonsterPosition(p);
                break;
            default:
                break;
        }
    }

    //EFFECTS: returns name of maze
    public String getName() {
        return name;
    }

    //EFFECTS: returns gridSize
    public int getGridSize() {
        return grid.getSize();
    }

    //EFFECTS: returns the position of the player
    public Position getPlayerPosition() {
        return grid.getPlayerPos();
    }

    //EFFECTS: returns the entity at the position
    //         empty, object, player, or monster
    public String getStatus(int x, int y) throws OutOfBoundsException {
        return grid.getStatus(new Position(x, y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Maze maze = (Maze) o;
        return name.equals(maze.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("grid", grid.toJson());
        return json;
    }


}