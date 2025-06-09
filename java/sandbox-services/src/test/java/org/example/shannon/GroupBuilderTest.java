package org.example.shannon;

import java.util.List;

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
        var a = new Element(String.valueOf(0b1111111111), "Mate");
        var b = new Element(String.valueOf(0b1111111111), "Dorka");
        var c = new Element(String.valueOf(0b1111111111), "Lili");
        var d = new Element(String.valueOf(0b1111111101), "Konstantin");
        var e = new Element(String.valueOf(0b1111111101), "Lajos");
        var f = new Element(String.valueOf(0b1111111101), "Gergo");
        var g = new Element(String.valueOf(0b1111111011), "Peti");
        var h = new Element(String.valueOf(0b1111111011), "Zoli");
        var j = new Element(String.valueOf(0b1111111011), "Balazs");
        var k = new Element(String.valueOf(0b1111110111), "Armin");
        var l = new Element(String.valueOf(0b1111110111), "Mark");
        var m = new Element(String.valueOf(0b1111110111), "Shailesh");

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
        Assertions.assertEquals(expected, actual);
        //Assertions.assertEquals(expected, actualFromReversed);
    }
}
