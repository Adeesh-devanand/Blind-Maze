/*

 */

package model;

public class Maze {
    private final String name;
    private Grid grid;
    private final int gridSize;

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

    //EFFECTS: Creates a copy of the Maze
    public Maze(Maze oldMaze) {
        this.name = oldMaze.name; //you can access
        Grid oldGrid = oldMaze.grid; //you can access
        this.grid = new Grid(oldGrid);
        this.gridSize = oldMaze.getGridSize();
    }

    //MODIFIES: this
    //EFFECTS: Moves Player
    public boolean movePlayer(String dir) {
        Position monsterP = grid.getMonsterPos();
        Position playerP = grid.getPlayerPos();
        if (playerP.equals(monsterP)) {
            return true;
        } else {
            grid.movePlayer(dir);
            return false;
        }
    }


    public void placeEntity(int y, int x, String entity) throws ElementAlreadyExistsException {
        Position p = new Position(y, x);
        switch (entity) {
            case "p":
            case "o":
            case "m":
                if (!grid.isEmpty(p)) {
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

    //EFFECTS: Returns Position of Player in Column i.e. (x) first, Row i.e.(y) second fashion
    public int[] getPlayerPosition() {
        Position p = grid.getPlayerPos();
        Position monsterP = grid.getMonsterPos();
        return new int[]{p.getPosY(), p.getPosX()};
    }

    public String getStatus(int y, int x) throws IndexOutOfBoundsException {
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
            throw new IndexOutOfBoundsException();
        }
        Position p = new Position(y, x);
        return grid.getStatus(p);
    }
}