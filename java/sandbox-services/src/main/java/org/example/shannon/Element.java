package org.example.shannon;

import java.util.Objects;

public class Element {
  private final String name;
  private final String attrib;

  public Element(String attrib, String name) {
    this.attrib = attrib;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Element element = (Element) o;
    return Objects.equals(name, element.name) && Objects.equals(attrib, element.attrib);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, attrib);
  }

  @Override
  public String toString() {
    return "Element{" + "name='" + name + '\'' + ", attrib='" + attrib + '\'' + '}';
  }

  public String getAttrib() {
    return attrib;
  }
}
