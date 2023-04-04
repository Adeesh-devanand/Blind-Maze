package model.grid;

public abstract class Element {
    private final String type;
    private String icon;

    public Element(String type) {
        this.type = type;
    }

    public Element(Element e) {
        this.type = e.type;
        this.icon = e.icon;
    }

    public String getType() {
        return type;
    }



    @Override
    public String toString() {
        return type;
    }
}
