package model.elements;

import model.Position;

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

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }
}
