package model.grid;

import model.Position;
import org.json.JSONObject;
import persistence.Writable;

public abstract class Element {
    private Position position;
    private String type;
    private String icon;

    protected Element(Position position, String type) {
        this.type = type;
        this.position = position;
    }

    protected Element(Element e) {
        this.type = e.type;
        this.icon = e.icon;
        this.position = e.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected String getIcon() {
        return icon;
    }

    protected String getType() {
        return type;
    }

    protected Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return type;
    }
}
