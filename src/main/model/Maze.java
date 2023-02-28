/*
* A typical controller, handles all interaction between UI, and the model
* the UI is only dependent on this class in the model package, all instructions
* to the model are fed through this class */

package model;

import model.exceptions.ElementAlreadyExistsException;
import model.exceptions.OutOfBoundsException;

public class Maze {
    private final String name;
    private final int gridSize;
    private final Grid grid;

    //EFFECTS: Creates a blank Maze with a given name
    public Maze(String name) {
        this(name, 10);
    }

    //EFFECTS: Creates a blank Maze with a given name and size
    public Maze(String name, int gridSize) {
        this.name = name;
        this.gridSize = gridSize;
        grid = new Grid(gridSize, gridSize);
    }

    //EFFECTS: Creates a deep copy of the given Maze
    public Maze(Maze oldMaze) {
        this.name = oldMaze.name; //you can access
        this.grid = new Grid(oldMaze.grid);//you can access
        this.gridSize = oldMaze.getGridSize();
    }

    //MODIFIES: this
    //EFFECTS: - moves player on the grid if it is a valid move,
    //         - returns whether the player made contact with the monster
    public boolean movePlayer(String dir) {
        grid.movePlayer(dir);

        Position monsterP = grid.getMonsterPos();
        int monsterY = monsterP.getPosX();
        int monsterX = monsterP.getPosX();

        Position playerP = grid.getPlayerPos();
        int playerY = playerP.getPosY();
        int playerX = playerP.getPosX();

        return monsterY == playerY && monsterX == playerX;
    }

    //TODO: Need to add OutOfBoundsException similar to getStatus() method
    //REQUIRES: y, and x should be in the grid limit
    //MODIFIES: this
    //EFFECTS: places an entity on the grid
    public void placeEntity(int y, int x, String entity) throws ElementAlreadyExistsException, OutOfBoundsException {
        Position p = new Position(y, x);
        switch (entity) {
            case "p":
            case "o":
            case "m":
                if (!grid.isCellEmpty(p)) {
                    throw new ElementAlreadyExistsException();
                }
                if (entity.equals("o")) {
                    grid.placeObstacle(p);
                } else if (entity.equals("m")) {
                    grid.clearCell(grid.getMonsterPos());
                    grid.placeMonster(p);
                } else {
                    grid.clearCell(grid.getPlayerPos());
                    grid.placePlayer(p);
                }
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
        return gridSize;
    }

    //EFFECTS: returns the position of the player
    public int[] getPlayerPosition() {
        Position p = grid.getPlayerPos();
        return new int[]{p.getPosY(), p.getPosX()};
    }

    //EFFECTS: returns the entity at the position
    //         - [e, o, p, m] for empty, object, player, and monster
    public String getStatus(int y, int x) throws OutOfBoundsException {
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
            throw new OutOfBoundsException();
        }
        Position p = new Position(y, x);
        return grid.getStatus(p);
    }
}