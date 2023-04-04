package model.grid;

import model.Position;

public class Player extends MovableElement {

    public Player(Position position, Grid grid) {
        super(position, "Player", grid);
    }

    public Player(Player player) {
        super(player);
    }
}
