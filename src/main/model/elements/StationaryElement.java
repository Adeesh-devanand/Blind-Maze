package model.elements;

import model.Position;

public abstract class StationaryElement extends Element {

    protected StationaryElement(String type) {
        super(null, type);
    }

    @Override
    public void setPosition(Position position){}
}
