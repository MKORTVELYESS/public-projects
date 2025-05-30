package org.example.shannon;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class GroupBuilder {
  public static List<Group> createMixedGroups(List<Element> fromElements, Integer numberOfGroups) {
    var groupCapacity = fromElements.size() / numberOfGroups;
    var emptyGroups =
        Stream.generate(() -> new Group(groupCapacity)).limit(numberOfGroups).toList();

    return insertElementsToRealizeHighestMixing(emptyGroups, fromElements);
  }

  private static List<Group> insertElementsToRealizeHighestMixing(
      List<Group> currentGroups, List<Element> elementsToInsert) {
    if (elementsToInsert.isEmpty()) {
      return currentGroups;
    } else if (elementsToInsert.size() == 1) {
      var head = elementsToInsert.getFirst();
      var sortedGroups = sortedGroupsFromHighestToLowestShannonIndexChange(currentGroups, head);
      return addElementToFirstGroup(sortedGroups, head);
    } else {
      var head = elementsToInsert.getFirst();
      var tail = List.copyOf(elementsToInsert.subList(1, elementsToInsert.size()));
      var sortedGroups = sortedGroupsFromHighestToLowestShannonIndexChange(currentGroups, head);
      var updatedGroups = addElementToFirstGroup(sortedGroups, head);
      return insertElementsToRealizeHighestMixing(updatedGroups, tail);
    }
  }

  private static List<Group> sortedGroupsFromHighestToLowestShannonIndexChange(
      List<Group> currentGroups, Element insertable) {
    return currentGroups.stream()
        .sorted(
            Comparator.comparingDouble(
                grp ->
                    grp.isFull()
                        ? -1.0
                        : grp.addMember(insertable).shannonIndex() - grp.shannonIndex()))
        .toList()
        .reversed();
  }

  private static List<Group> addElementToFirstGroup(List<Group> currentGroups, Element insertable) {
    var head = currentGroups.getFirst().addMember(insertable);
    var tail = List.copyOf(currentGroups.subList(1, currentGroups.size()));

    return Stream.concat(Stream.of(head), tail.stream()).toList();
  }
}
