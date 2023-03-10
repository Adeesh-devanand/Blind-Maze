package model.elements;

import model.Position;

public class Player extends Element {

    public Player(Position position) {
        super(position, "Player");
    }

    public Player(Player player) {
        super(player);
    }
}
