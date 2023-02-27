/*

 */

package model;

public class Grid {
    private final int upperX;
    private final int upperY;
    private int[][] grid;

    private Position playerPos;
    private Position monsterPos;


    //EFFECTS: Creates an Empty Grid of size (y by x)
    public Grid(int y, int x) {
        upperY = y;
        upperX = x;
        grid = new int[y][x];

        playerPos = new Position(0, 0);
        monsterPos = new Position(y, x);

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                grid[i][j] = 0;
            }
        }

        grid[0][0] = 2;
        grid[y - 1][x - 1] = 3;
    }

    //EFFECTS: Returns a copy of the given Grid object
    public Grid(Grid oldGrid) {
        this.upperY = oldGrid.upperY; //you can access
        this.upperX = oldGrid.upperX; //you can access
        this.grid = new int[upperY][upperX];
        int[][] old = oldGrid.grid;

        for (int i = 0; i < this.upperY; i++) {
            System.arraycopy(old[i], 0, this.grid[i], 0, this.upperX);
        }

        //Position can't be modified after initialized so
        // no need deep copy even if it is an object
        this.playerPos = oldGrid.playerPos;
        this.monsterPos = oldGrid.monsterPos;
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //MODIFIES: this
    //EFFECTS: Places Obstacle on Grid
    public void placeObstacle(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 1;
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //
    //MODIFIES: this
    //EFFECTS: Places Player on Grid
    public void placePlayer(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 2;

        playerPos = new Position(y, x);
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //MODIFIES: this
    //EFFECTS: Places Monster on Grid
    public void placeMonster(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 3;

        monsterPos = new Position(y, x);
    }

    //REQUIRES: dir should be one of "l", "r", "u", "d"
    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid move, else doesn't do anything
    public void movePlayer(String dir) {
        Position playerPos = getPlayerPos();
        int oldY = playerPos.getPosY();
        int oldX = playerPos.getPosX();

        this.playerPos = moveEntity(dir, oldY, oldX, 2);
    }

    //REQUIRES: should be a valid move ~otherwise monster might get stuck~
    //MODIFIES: this
    //EFFECTS: Moves Monster on Grid
    public void moveMonster(String dir) {
        Position monsterPos = getMonsterPos();
        int oldY = monsterPos.getPosY();
        int oldX = monsterPos.getPosX();

        this.monsterPos = moveEntity(dir, oldY, oldX, 3);
    }

    //EFFECTS: returns Position of Player
    public Position getPlayerPos() {
        return playerPos;
    }

    //EFFECTS: returns Position of Monster
    public Position getMonsterPos() {
        return monsterPos;
    }

    //EFFECTS: Clears the cell
    public void clearCell(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 0;
    }

    //EFFECTS: returns the status at given grid Position
    //String is one of "o", "p", "m", "e"
    public String getStatus(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        int elementInt = grid[y][x];
        String elementString;

        switch (elementInt) {
            case 0:
                elementString = "e";
                break;
            case 1:
                elementString = "o";
                break;
            case 2:
                elementString = "p";
                break;
            default:
                elementString = "m";
        }

        return elementString;
    }

    private Position moveEntity(String dir, int oldY, int oldX, int entity) {
        int newY = oldY;
        int newX = oldX;

        switch (dir) {
            case "r":
                newX += 1;
                break;
            case "l":
                newX -= 1;
                break;
            case "u":
                newY -= 1;
                break;
            case "d":
                newY += 1;
            default:
                break;
        }

        if (newX >= 0 && newY >= 0 && newX < upperX && newY < upperY && grid[newY][newX] == 0) {
            grid[oldY][oldX] = 0;
            grid[newY][newX] = entity;
            return new Position(newY, newX);
        }

        return new Position(oldY, oldX);
    }

    public boolean isEmpty(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        int elementInt = grid[y][x];
        return elementInt == 0;
    }
}