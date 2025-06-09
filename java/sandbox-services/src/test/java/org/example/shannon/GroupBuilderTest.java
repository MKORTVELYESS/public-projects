package org.example.shannon;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GroupBuilderTest {
  @Test
  void testCreateMixedGroups() {
    var a = new Element("NEMR", "Mate");
    var b = new Element("NEMR", "Dorka");
    var c = new Element("NEMR", "Lili");
    var d = new Element("ANALYTICS", "Konstantin");
    var e = new Element("ANALYTICS", "Lajos");
    var f = new Element("AVENGERS", "Gergo");
    var g = new Element("AVENGERS", "Peti");
    var h = new Element("AVENGERS", "Zoli");
    var j = new Element("AVENGERS", "Balazs");
    var k = new Element("CODX", "Armin");
    var l = new Element("VCCOM", "Mark");
    var m = new Element("ATOM", "Shailesh");

    var ppl = List.of(a, b, c, d, e, f, g, h, j, k, l, m);

    var expected =
        List.of(
            new Group(3).addMember(d).addMember(f).addMember(m),
            new Group(3).addMember(b).addMember(h).addMember(k),
            new Group(3).addMember(c).addMember(e).addMember(j),
            new Group(3).addMember(a).addMember(g).addMember(l));

    var actual = GroupBuilder.createMixedGroups(ppl, 4);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testCreateMixedGroupsAddingEmptyList() {

    List<Element> ppl = List.of();

    var actual = GroupBuilder.createMixedGroups(ppl, 2);

    var expected = List.of(new Group(0), new Group(0));

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testCreateSimilarGroups() {
    var a = new Element((short) 0b1111111111, "Mate");
    var b = new Element((short) 0b1111111111, "Dorka");
    var c = new Element((short) 0b1111111111, "Lili");
    var d = new Element((short) 0b1111111101, "Konstantin");
    var e = new Element((short) 0b1111111101, "Lajos");
    var f = new Element((short) 0b1111111101, "Gergo");
    var g = new Element((short) 0b1111111011, "Peti");
    var h = new Element((short) 0b1111111011, "Zoli");
    var j = new Element((short) 0b1111111011, "Balazs");
    var k = new Element((short) 0b1111110111, "Armin");
    var l = new Element((short) 0b1111110111, "Mark");
    var m = new Element((short) 0b1111110111, "Shailesh");

    var ppl = List.of(a, b, c, d, e, f, g, h, j, k, l, m);
    var pplReversed = List.of(m, l, k, j, h, g, f, e, d, c, b, a);

    var expected =
        List.of(
            new Group(3).addMember(d).addMember(e).addMember(f),
            new Group(3).addMember(a).addMember(b).addMember(c),
            new Group(3).addMember(m).addMember(l).addMember(k),
            new Group(3).addMember(j).addMember(h).addMember(g));

    var actual = GroupBuilder.createSimilarGroups(ppl, 4);
    var actualFromReversed = GroupBuilder.createSimilarGroups(pplReversed, 4);

    Assertions.assertTrue(actual.containsAll(expected));
    Assertions.assertTrue(actualFromReversed.containsAll(expected));
  }

  @Test
  void testCreateSimilarGroupsWithProximity() {
    // proximity 9
    var a = new Element((short) 0b1100011101, "Mate");
    var b = new Element((short) 0b1101011101, "Dorka");
    var c = new Element((short) 0b1100011001, "Lili");

    // proximity 7
    var d = new Element((short) 0b1111111101, "Konstantin");
    var e = new Element((short) 0b0011111001, "Lajos");
    var f = new Element((short) 0b1101111011, "Gergo");

    // proximity 5
    var g = new Element((short) 0b0001110001, "Peti");
    var h = new Element((short) 0b1111110010, "Zoli");
    var j = new Element((short) 0b0101111110, "Balazs");

    // proximity 3
    var k = new Element((short) 0b0101110010, "Armin");
    var l = new Element((short) 0b1100001110, "Mark");
    var m = new Element((short) 0b1001101101, "Shailesh");

    var ppl = List.of(a, b, c, d, e, f, g, h, j, k, l, m);

    var expected =
        List.of(
            new Group(3).addMember(e).addMember(h).addMember(k),
            new Group(3).addMember(l).addMember(j).addMember(f),
            new Group(3).addMember(a).addMember(c).addMember(b),
            new Group(3).addMember(m).addMember(d).addMember(g));

    var actual = GroupBuilder.createSimilarGroups(ppl, 4);
    Assertions.assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    actual.forEach(
        grp -> System.out.println("avg: " + grp.avgProximity() + " stdev: " + grp.stdProximity()));
    var strings =
        actual.stream()
            .map(
                grp -> {
                  var builder = new StringBuilder();
                  grp.getMembers().forEach(mem -> builder.append(mem.getName()).append(" "));
                  return builder.toString();
                })
            .collect(Collectors.toList());
    System.out.println(strings);
  }
}
