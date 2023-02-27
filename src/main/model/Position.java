/*
* Acts as a more compact way to keep track of a cell on a grid
* NOTE: All positions start from the top left corner i.e top left is (0, 0)
* 'y' represents the y axes and comes first unlike the co-ordinate system
* think of it exactly as a multidimensional array with rows and columns in programming */

package model;

public class Position {
    private final int posY;
    private final int posX;

    public Position(int y, int x) {
        posY = y;
        posX = x;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }
}
