/*
 * A typical controller, handles all interaction between UI, and the model
 * the UI is only dependent on this class in the model package, all instructions
 * to the model are fed through this class */

package model;

import model.grid.Grid;
import model.exceptions.ContactException;
import model.exceptions.PositionOccupiedException;
import model.exceptions.OutOfBoundsException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;
import java.util.Random;

public class Maze implements Writable {
    private final String name;
    private final Grid grid;
    private final int id;

    //EFFECTS: Creates a blank Maze with a given name and size
    public Maze(String name, int gridSize) {
        this.name = name;
        this.id = new Random().nextInt();
        grid = new Grid(gridSize);
    }

    //EFFECTS: Creates a deep copy of the given Maze
    public Maze(Maze oldMaze) {
        this.name = oldMaze.name;
        this.id = oldMaze.id;
        this.grid = new Grid(oldMaze.grid);
    }

    //Requires: mazeJson must be a valid maze Json created using toJson()
    //Effects: translates the JSONObject into a Maze
    public Maze(JSONObject mazeJson) {
        JSONObject gridJson = mazeJson.getJSONObject("grid");
        this.name = mazeJson.getString("name");
        this.id = mazeJson.getInt("id");
        this.grid = new Grid(gridJson);
    }

    //Modifies: this
    //Effects: - moves the Player in the given direction if it's within bounds,
    //           and if the new position is empty.
    //         - throws ContactException on contact with Monster
    public void movePlayer(String dir) throws ContactException {
        grid.movePlayer(dir);
    }

    //Modifies: this
    //Effects: moves the Cursor in the given direction if it's within bounds
    public void moveCursor(String dir) {
        grid.moveCursor(dir);
    }

    //Modifies: this
    //Effects: moves the Cursor to the given Position if it's within bounds
    public void setCursorPos(Position pos) throws OutOfBoundsException {
        grid.setCursorPosition(pos);
    }

    //Requires: the Position must be empty
    //Modifies: this
    //Effects: places an entity on the grid at cursor pos
    public void placeEntity(String entity) throws PositionOccupiedException {
        Position p = grid.getCursorPos();
        try {
            switch (entity.toLowerCase()) {
                case "player":
                    grid.setPlayerPosition(p);
                    break;
                case "obstacle":
                    grid.placeObstacle(p);
                    break;
                case "monster":
                    grid.setMonsterPosition(p);
                    break;
            }
        } catch (OutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    //EFFECTS: returns name of maze
    public String getName() {
        return name;
    }

    //EFFECTS: returns the size of the maze
    public int getGridSize() {
        return grid.getSize();
    }

    //EFFECTS: returns the position of the player
    public Position getPlayerPosition() {
        return grid.getPlayerPos();
    }

    //Requires: the Position is within bounds
    //EFFECTS:  returns the entity type at the given position
    public String getStatus(Position p) throws OutOfBoundsException {
        return grid.getStatus(p);
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
        return id == maze.id && name.equals(maze.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("grid", grid.toJson());
        return json;
    }


}