package org.example.shannon;

import java.util.Objects;

public class Element {
  private final String name;
  private final String attrib;
  private final short personalityBits;

  public String getName() {
    return name;
  }

  public Element(String attrib, String name) {
    this.attrib = attrib;
    this.name = name;
    this.personalityBits = 0b0000000000;
  }

  public Element(short personalityBits, String name) {
    this.attrib = "";
    this.name = name;
    this.personalityBits = personalityBits;
  }

  public short getPersonalityBits() {
    return personalityBits;
  }

  public int hummingSimilarity(Element other) {
    return 10 - Integer.bitCount(this.getPersonalityBits() ^ other.getPersonalityBits());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Element element = (Element) o;
    return Objects.equals(name, element.name)
        && Objects.equals(attrib, element.attrib)
        && Objects.equals(personalityBits, element.personalityBits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, attrib, personalityBits);
  }

  @Override
  public String toString() {
    return "Element{"
        + "name='"
        + name
        + '\''
        + ", attrib='"
        + attrib
        + '\''
        + ", personalityBits="
        + String.format("%10s", Integer.toBinaryString(personalityBits & 0x3FF)).replace(' ', '0')
        + '}';
  }

  public String getAttrib() {
    return attrib;
  }
}
