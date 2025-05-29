package org.example.shannon;

public class Element {
    private final String name;
    private final String attrib;

    public Element(String attrib, String name) {
        this.attrib = attrib;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Element{" +
                "name='" + name + '\'' +
                ", attrib='" + attrib + '\'' +
                '}';
    }

    public String getAttrib() {
        return attrib;
    }

}
