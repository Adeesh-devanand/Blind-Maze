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
    //EFFECTS: Starts time from 0
    public void startTime() {
    }

    //EFFECTS: Return Time Elapsed
    public MazeTimer getTime() {
        return null;
    }

    //MODIFIES: this
    //EFFECTS: Pause Time
    public void pauseTime() {
    }

    //MODIFIES: this
    //EFFECTS: Moves Player
    public void movePlayer(String dir) {
        grid.movePlayer(dir);
    }


    public void placeEntity(int y, int x, String entity) {
        Position p = new Position(y, x);
        switch (entity) {
            case "p":
                grid.placePlayer(p);
                break;
            case "o":
                grid.placeObstacle(p);
                break;
            case "m":
                grid.placeMonster(p);
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
        int[] arr = {p.getPosY(), p.getPosX()};
        return arr;
    }

//    //EFFECTS: Returns Position of Monster
//    public int[] getMonsterPosition() {
//        Position p = grid.getMonsterPos();
//        int[] arr = {p.getPosY(), p.getPosX()};
//        return arr;
//    }

    //EFFECTS: returns the status at given grid Position
    //String is one of "obstacle", "player", "monster", "empty"
    public String getStatus(int y, int x) throws IndexOutOfBoundsException {
        if (x < 0 || y < 0 || x >= gridSize || y >= gridSize) {
            throw new IndexOutOfBoundsException();
        }
        Position p = new Position(y, x);
        return grid.getStatus(p);
    }

    //MODIFIES: this
    //EFFECTS: End Maze Game(Reached Exit)
    public void endGame() {
    }

    //MODIFIES: this
    //EFFECTS: Exit Maze
    public void exitMaze() {
    }
}
