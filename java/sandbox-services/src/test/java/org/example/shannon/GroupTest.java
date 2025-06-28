package org.example.shannon;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import org.example.domain.shannon.Element;
import org.example.domain.shannon.Group;
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

    var d = new Element((short) 0b1111111101, "Konstantin");
    var e = new Element((short) 0b1111111101, "Lajos");
    var f = new Element((short) 0b1111111101, "Gergo");
    var g = new Element((short) 0b1111111011, "Peti");

    var grp3 = new Group(3).addMember(d).addMember(e).addMember(f);
    var grp3Copy = new Group(3).addMember(d).addMember(e).addMember(f);
    var grp4 = new Group(3).addMember(e).addMember(f).addMember(d);
    var grp5 = new Group(3).addMember(e).addMember(f).addMember(g);

    var grpList1 = List.of(grp3, grp4);
    var grpList2 = List.of(grp4, grp3);
    var grpList3 = List.of(grp3, grp5);
    var grpList4 = List.of(grp5, grp3Copy);

    assertFalse(grp.equals(null));
    assertFalse(grp.equals(elem));
    assertEquals(grp, grp);
    assertEquals(grp, grp2);
    assertEquals(grp3, grp4);
    assertEquals(grpList1, grpList2);
    assertNotEquals(grpList3, grpList4);
    assertEquals(new HashSet<>(grpList3), new HashSet<>(grpList4));
  }

  @Test
  void testAvgProximity() {
    var a = new Element((short) 0b1111111101, "Konstantin");
    var b = new Element((short) 0b1111111101, "Lajos");
    var c = new Element((short) 0b1111111100, "Mate");

    var grp = new Group(3).addMember(a).addMember(b);
    var grp2 = grp.addMember(c);

    assertEquals(10.0, grp.avgProximity());
    assertEquals(9.333333333333334, grp2.avgProximity());
  }

  @Test
  void testStdProximity() {
    var a = new Element((short) 0b1111111101, "Konstantin");
    var b = new Element((short) 0b1111111101, "Lajos");
    var c = new Element((short) 0b1111111100, "Mate");

    var grp = new Group(3).addMember(a).addMember(b);
    var grp2 = grp.addMember(c);

    assertEquals(0.0, grp.stdProximity());
    assertEquals(0.23570226039551584, grp2.stdProximity());
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
