package org.example.shannon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ElementTest {

  @Test
  void testEquals() {
    var a = new Element("hasHead", "Kimmy");
    var b = new Element("hasHead", "Kimmy");
    var c = new Element("doesNotHaveHead", "Kimmy");
    var d = new Element("hasHead", "Eric");
    var different = new Group(5);

    assertFalse(a.equals(null));
    assertFalse(a.equals(different));

    assertEquals(a, a);
    assertEquals(a, b);
    assertNotEquals(a, c);
    assertNotEquals(a, d);
    assertNotEquals(c, d);
  }

  @Test
  void testHashCode() {
    var a = new Element("hasHead", "Kimmy");
    assertEquals(-1350874592, a.hashCode());
  }

  @Test
  void testToString() {
    var actual = new Element("hasHead", "Kimmy");
    var expected = "Element{name='Kimmy', attrib='hasHead'}";
    assertEquals(expected, actual.toString());
  }

  @Test
  void testHummingSimilarity() {
    var first = new Element((short) 0b0101010101, "Mate");
    var second = new Element((short) 0b0101010100, "Mate");
    var third = new Element((short) 0b0000000000, "Mate");
    var similarity1 = first.hummingSimilarity(second);
    var similarity2 = second.hummingSimilarity(third);
    assertEquals(9, similarity1);
    assertEquals(6, similarity2);
  }
}
