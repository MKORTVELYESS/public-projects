package org.example.shannon;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    public record Result(Integer groupIndex, Double changeInShannonIndex) {
    }

    ;

    @Test
    void isFull() {
        var a = new Element("NEMR","Mate");
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


        Group grp1 = new Group(3);
        Group grp2 = new Group(3);
        Group grp3 = new Group(3);
        Group grp4 = new Group(3);

        var grps = List.of(grp1, grp2, grp3, grp4);

        ppl.stream().forEach((person) -> {
            AtomicInteger index = new AtomicInteger(0);
            var chg = grps.stream().map((group) -> {
                var idx = index.getAndIncrement();
                if (!group.isFull()) {
                    var originalShannonIndex = group.shannonIndex();
                    var addedIndex = group.addMember(person);
                    var newShannonIndex = group.shannonIndex();
                    group.removeMember(addedIndex);
                    var changeInShannonIndex = newShannonIndex - originalShannonIndex;
                    return new Result(idx, changeInShannonIndex);
                } else {
                    return new Result(idx, -1.0);
                }
            });

            var largestIncreaseGroupIndex = chg.max(Comparator.comparing(Result::changeInShannonIndex)).get().groupIndex;
            grps.get(largestIncreaseGroupIndex).addMember(person);
        });

        grps.forEach(System.out::println);

    }

    @Test
    void shannonIndex() {
    }

    @Test
    void addMember() {
    }
}