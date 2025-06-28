package org.example.shannon;

import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.shannon.Element;
import org.example.domain.shannon.Group;
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
    var b = new Element((short) 0b0101010101, "Kimmy");
    assertEquals(1072560608, a.hashCode());
    assertEquals(953417615, b.hashCode());
  }

  @Test
  void testToString() {
    var actual = new Element("hasHead", "Kimmy");
    var actual2 = new Element((short) 0b0101010101, "Tommer");
    var expected = "Element{name='Kimmy', attrib='hasHead', personalityBits=0000000000}";
    var expected2 = "Element{name='Tommer', attrib='', personalityBits=0101010101}";
    assertEquals(expected, actual.toString());
    assertEquals(expected2, actual2.toString());
  }

  @Test
  void testHummingSimilarity() {
    var first = new Element((short) 0b0101010101, "Mate");
    var second = new Element((short) 0b0101010100, "Mate");
    var third = new Element((short) 0b0000000000, "Mate");
    var fourth = new Element((short) 0b0101110010, "Armin");
    var fifth = new Element((short) 0b1100001110, "Mark");
    var similarity1 = first.hummingSimilarity(second);
    var similarity2 = second.hummingSimilarity(third);
    var similarity3 = fourth.hummingSimilarity(fifth);
    var similarity4 = fifth.hummingSimilarity(fourth);
    assertEquals(9, similarity1);
    assertEquals(6, similarity2);
    assertEquals(4, similarity3);
    assertEquals(4, similarity4);
  }
}
