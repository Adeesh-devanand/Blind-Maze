/*

 */

package model;

public class Grid {
    private int upperX;
    private int upperY;

    private Position playerPos;
    private Position monsterPos;

    int[][] grid;

    //EFFECTS: Creates an Empty Grid of size (x by y)
    public Grid(int x, int y) {
        upperX = x;
        upperY = y;

        grid = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = 0;
            }
        }
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //MODIFIES: this
    //EFFECTS: Places Obstacle on Grid
    public void placeObstacle(Position p) {
        int x = p.getPosX();
        int y = p.getPosY();
        grid[x][y] = 1;
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //
    //MODIFIES: this
    //EFFECTS: Places Player on Grid
    public void placePlayer(Position p) {
        int x = p.getPosX();
        int y = p.getPosY();
        grid[x][y] = 2;

        playerPos = new Position(x, y);
    }

    //REQUIRES: Position p should be < Grid size, and empty
    //MODIFIES: this
    //EFFECTS: Places Monster on Grid
    public void placeMonster(Position p) {
        int x = p.getPosX();
        int y = p.getPosY();
        grid[x][y] = 3;

        monsterPos = new Position(x, y);
    }

    //MODIFIES: this
    //EFFECTS: Moves Player on Grid if it is a valid move, else doesn't do anything
    public void movePlayer(String dir) {
        Position playerPos = getPlayerPos();
        int oldX = playerPos.getPosX();
        int oldY = playerPos.getPosY();

        this.playerPos = moveEntity(dir, oldX, oldY, 2);
    }

    //REQUIRES: should be a valid move -otherwise monster might get stuck-
    //MODIFIES: this
    //EFFECTS: Moves Monster on Grid
    public void moveMonster(String dir) {
        Position monsterPos = getMonsterPos();
        int oldX = monsterPos.getPosX();
        int oldY = monsterPos.getPosY();

        this.monsterPos = moveEntity(dir, oldX, oldY, 3);
    }

    //EFFECTS: returns Position of Player
    public Position getPlayerPos() {
        return playerPos;
    }

    //EFFECTS: returns Position of Monster
    public Position getMonsterPos() {
        return monsterPos;
    }

    //EFFECTS: returns the status at given grid Position
    //String is one of "obstacle", "player", "monster", "empty"
    public String getStatus(Position p) {
        int x = p.getPosX();
        int y = p.getPosY();
        int elementInt = grid[x][y];
        String elementS;

        switch (elementInt) {
            case 0:
                elementS = "empty";
                break;
            case 1:
                elementS = "obstacle";
                break;
            case 2:
                elementS = "player";
                break;
            default:
                elementS = "monster";
        }

        return elementS;
    }

    private Position moveEntity(String dir, int oldX, int oldY, int entity) {
        int newX = oldX;
        int newY = oldY;

        switch (dir) {
            case "right":
                newX += 1;
                break;
            case "left":
                newX -= 1;
                break;
            case "up":
                newY -= 1;
                break;
            default:
                newY += 1;
        }

        if (newX < upperX && newY < upperY && grid[newX][newY] == 0) {
            grid[oldX][oldY] = 0;
            grid[newX][newY] = entity;
            return new Position(newX, newY);
        }

        return new Position(oldX, oldY);
    }
}