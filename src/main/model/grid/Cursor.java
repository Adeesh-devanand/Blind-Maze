package model.grid;

import model.Position;

public class Cursor extends Element {

    public Cursor(Position position) {
        super(position, "cursor");
    }

    public Cursor(Cursor cursor) {
        super(cursor);
    }
}
