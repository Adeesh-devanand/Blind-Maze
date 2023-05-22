/*
* Acts as a more compact way to keep track of a cell on a grid
* NOTE: All positions start from the top left corner i.e top left is (0, 0)
* 'y' represents the y axes and comes first unlike the co-ordinate system
* think of it exactly as a multidimensional array with rows and columns in programming */

package model;

import java.util.Objects;

public class Position {
    private final int posX;
    private final int posY;


    //Effects: creates a Position with given co-ords
    public Position(int x, int y) {
        posX = x;
        posY = y;
    }

    //Requires: str was created using Position.toString()
    //Effects:  creates a Position with given co-ords
    public Position(String str) {
        String[] pos = str.split(" ");
        this.posX = Integer.parseInt(pos[0]);
        this.posY = Integer.parseInt(pos[1]);
    }

    //Effects: returns the x co-ord
    public int getPosX() {
        return posX;
    }

    //Effects: returns the y co-ord
    public int getPosY() {
        return posY;
    }


    //Effects: - returns the new position in the given direction
    //         - the Position doesn't need to be withing the grid
    public static Position getNewPosition(Position oldPos, String dir) {
        int newX = oldPos.getPosX();
        int newY = oldPos.getPosY();

        switch (dir.toLowerCase()) {
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
        }
        return new Position(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return posY == position.posY && posX == position.posX;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posY, posX);
    }

    @Override
    public String toString() {
        return posX + " " + posY;
    }
}
