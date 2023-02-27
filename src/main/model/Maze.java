/*

 */

package model;

public class Maze {
    private final String name;
    private final int gridSize;
    private Grid grid;

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
        Grid oldGrid = oldMaze.grid; //you can access
        this.grid = new Grid(oldGrid);
        this.gridSize = oldMaze.getGridSize();
    }

    //MODIFIES: this
    //EFFECTS: - moves player on the grid if it is a valid move,
    //         - returns whether the player made contact with the monster
    public boolean movePlayer(String dir) {
        Position monsterP = grid.getMonsterPos();
        int monsterY = monsterP.getPosX();
        int monsterX = monsterP.getPosX();

        Position playerP = grid.getPlayerPos();
        int playerY = playerP.getPosY();
        int playerX = playerP.getPosX();

        grid.movePlayer(dir);
        if (monsterY == playerY && monsterX == playerX) {
            return true;
        } else {
            return false;
        }
    }

    //TODO: Need to add OutOfBoundsException similar to getStatus() method
    //REQUIRES: y, and x should be in the grid limit
    //MODIFIES: this
    //EFFECTS: places an entity on the grid
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

    //EFFECTS: returns the position of the player
    public int[] getPlayerPosition() {
        Position p = grid.getPlayerPos();
        Position monsterP = grid.getMonsterPos();
        return new int[]{p.getPosY(), p.getPosX()};
    }

    //EFFECTS: returns the entity at the position
    //         - [e, o, p, m] for empty, object, player, and monster
    public String getStatus(int y, int x) throws IndexOutOfBoundsException {
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
            throw new IndexOutOfBoundsException();
        }
        Position p = new Position(y, x);
        return grid.getStatus(p);
    }
}