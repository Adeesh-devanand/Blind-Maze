/*
* Acts as a more compact way to keep track of a cell on a grid
* NOTE: All positions start from the top left corner i.e top left is (0, 0)
* 'y' represents the y axes and comes first unlike the co-ordinate system
* think of it exactly as a multidimensional array with rows and columns in programming */

package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

public class Position implements Writable {
    private final int posX;
    private final int posY;

    public Position(int x, int y) {
        posX = x;
        posY = y;
    }

    public Position(String str) {
        String[] pos = str.split(" ");
        this.posX = Integer.parseInt(pos[0]);
        this.posY = Integer.parseInt(pos[1]);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
    public JSONObject toJson() {
        return null;
    }

    @Override
    public String toString() {
        return posX + " " + posY;
    }
}
