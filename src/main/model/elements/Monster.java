package model.elements;

import model.Position;

public class Monster extends Element {

    public Monster(Position position) {
        super(position, "Monster");
    }

    public Monster(Monster monster) {
        super(monster);
    }
}
