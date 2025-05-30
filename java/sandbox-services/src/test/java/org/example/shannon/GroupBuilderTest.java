package org.example.shannon;

import java.util.List;
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

    var groups = GroupBuilder.createMixedGroups(ppl, 4);

    System.out.println(groups);
  }
}
