/*

 */

package model;

public class Grid {
    private final int upperX;
    private final int upperY;
    private final int[][] grid;

    private Position playerPos;
    private Position monsterPos;


    //EFFECTS: Creates an Empty Grid of size (y down by x across)
    public Grid(int y, int x) {
        upperY = y;
        upperX = x;
        grid = new int[y][x];

        playerPos = new Position(0, 0);
        monsterPos = new Position(y - 1, x - 1);

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                grid[i][j] = 0;
            }
        }

        grid[0][0] = 2;
        grid[y - 1][x - 1] = 3;
    }

    //EFFECTS: Creates a copy of the given Grid object
    public Grid(Grid oldGrid) {
        this.upperY = oldGrid.upperY; //you can access
        this.upperX = oldGrid.upperX; //you can access
        this.grid = new int[upperY][upperX];

        for (int i = 0; i < this.upperY; i++) {
            System.arraycopy(oldGrid.grid[i], 0, this.grid[i], 0, this.upperX);
        }

        //Position can't be modified after initialized so
        // no need deep copy even if it is an object
        this.playerPos = oldGrid.playerPos;
        this.monsterPos = oldGrid.monsterPos;
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Obstacle on grid at given position
    public void placeObstacle(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 1;
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Player on grid at given position
    public void placePlayer(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 2;

        playerPos = new Position(y, x);
    }

    //TODO: resolve conflict when monster and player collide
    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid move, else doesn't do anything
    public void movePlayer(String dir) {
        Position playerPos = getPlayerPos();
        this.playerPos = moveEntity(dir, playerPos, 2);
    }

    //EFFECTS: returns Position of Player
    public Position getPlayerPos() {
        return playerPos;
    }

    //REQUIRES: Position p should be empty, and be within grid limits
    //MODIFIES: this
    //EFFECTS: Places Monster on grid at given position
    public void placeMonster(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 3;

        monsterPos = new Position(y, x);
    }

    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid move, else doesn't do anything
    public void moveMonster(String dir) {
        Position monsterPos = getMonsterPos();
        this.monsterPos = moveEntity(dir, monsterPos, 3);
    }

    //EFFECTS: returns Position of Monster
    public Position getMonsterPos() {
        return monsterPos;
    }

    //REQUIRES: Position is withing grid limits
    //EFFECTS: returns the status at given grid Position
    //String is one of "o", "p", "m", "e"//
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

    //TODO: resolve conflict when monster and player collide
    //TODO: TEMP-EFFECTS: moves an entity on the map if there is no obstacle
    //REQUIRES: entity should be the integer encoding for an entity
    //MODIFIES: this
    //EFFECTS: moves an entity on the map if there is no other element present, and returns the new position
    @SuppressWarnings("methodlength")
    private Position moveEntity(String dir, Position oldPos, int entity) {
        int oldY = oldPos.getPosY();
        int oldX = oldPos.getPosX();
        int newY = oldY;
        int newX = oldX;

        switch (dir) {
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
            default:
        }

        if (newX >= 0 && newY >= 0 && newX < upperX && newY < upperY && grid[newY][newX] != 1) {
            grid[oldY][oldX] = 0;
            grid[newY][newX] = entity;
            return new Position(newY, newX);
        }

        return new Position(oldY, oldX);
    }

    //REQUIRES: p shouldn't be the position of a player or monster
    //MODIFIES: this
    //EFFECTS: Clears the Position on the grid
    public void clearCell(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        grid[y][x] = 0;
    }

    //EFFECTS: returns if the given position is empty or not
    public boolean isEmpty(Position p) {
        int y = p.getPosY();
        int x = p.getPosX();
        int elementInt = grid[y][x];
        return elementInt == 0;
    }
}