package org.example.shannon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GroupTest {
  Group grp = new Group(0);

  @Test
  void isFull() {
    assertThrows(
        IndexOutOfBoundsException.class,
        () -> {
          Group newGrp = grp.addMember(new Element("dummy", "dummy"));
        });
  }

  @Test
  void testEquals() {
    var grp2 = new Group(0);
    var elem = new Element("dummy", "dum");
    assertFalse(grp.equals(null));
    assertFalse(grp.equals(elem));
    assertEquals(grp, grp);
    assertEquals(grp, grp2);
  }

  @Test
  void testHashCode() {
    assertEquals(32, grp.hashCode());
  }

  @Test
  void testToString() {
    assertEquals(
        "Group{maxCapacity=0, members=[], currentUsage=0, attributeFrequency={}, groupId="
            + grp.getGroupId()
            + "}",
        grp.toString());
  }
}
