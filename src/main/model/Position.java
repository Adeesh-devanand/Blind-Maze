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
