package org.example.shannon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Group {
  private final Integer maxCapacity;
  private final List<Element> members;
  private final Integer currentUsage;
  private final Map<String, Integer> attributeFrequency;
  private final UUID groupId;

  public UUID getGroupId() {
    return groupId;
  }

  public Group(Integer maxCapacity) {
    this.currentUsage = 0;
    this.maxCapacity = maxCapacity;
    this.members = new ArrayList<>(maxCapacity);
    this.groupId = UUID.randomUUID();
    this.attributeFrequency = new HashMap<>();
  }

  private Group(
      Integer maxCapacity,
      List<Element> members,
      Integer currentUsage,
      Map<String, Integer> attributeFrequency,
      UUID groupId) {
    this.maxCapacity = maxCapacity;
    this.members = members;
    this.currentUsage = currentUsage;
    this.attributeFrequency = attributeFrequency;
    this.groupId = groupId;
  }

  public Group copy(
      List<Element> members, Integer currentUsage, Map<String, Integer> attributeFrequency) {

    return new Group(
        this.maxCapacity,
        List.copyOf(members),
        currentUsage,
        Map.copyOf(attributeFrequency),
        this.groupId);
  }

  Boolean isFull() {
    return Objects.equals(currentUsage, maxCapacity);
  }

  private Integer getCurrentAttributeFrequency(String attribute) {
    return attributeFrequency.getOrDefault(attribute, 0);
  }

  public Double shannonIndex() {

    if (currentUsage == 0) {
      return -1.0;
    } else {

      var weightedGeomean =
          attributeFrequency.values().stream()
              .map(
                  (freq) -> {
                    var p = (double) freq / currentUsage;
                    return Math.pow(p, p);
                  })
              .reduce(1.0, (accumulator, next) -> accumulator * next);

      return Math.log((1 / weightedGeomean));
    }
  }

  private DoubleStream individualProximities() {
    return members.stream()
        .mapToDouble(
            member ->
                members.stream()
                    .filter(mem -> !mem.equals(member))
                    .mapToInt(member::hummingSimilarity)
                    .average()
                    .orElse(0.0));
  }

  public Double avgProximity() {
    return individualProximities().average().orElse(0.0);
  }

  public Double stdProximity() {
    var avg = avgProximity();
    var variance =
        individualProximities().map(proximity -> Math.pow(proximity - avg, 2)).average().orElse(0);
    return Math.sqrt(variance);
  }

  public Group addMember(Element e) throws IndexOutOfBoundsException {
    if (!isFull()) {
      var newMembers =
          Stream.concat(this.members.stream(), Stream.of(e)).collect(Collectors.toList());

      var mutableMapCopy = new HashMap<>(Map.copyOf(this.attributeFrequency));
      mutableMapCopy.put(e.getAttrib(), getCurrentAttributeFrequency(e.getAttrib()) + 1);

      return this.copy(newMembers, this.currentUsage + 1, mutableMapCopy);
    } else {
      throw new IndexOutOfBoundsException("Can not add more members as this group is at capacity");
    }
  }

  public Group addMembers(List<Element> elems) throws IndexOutOfBoundsException {
    if (elems.isEmpty()) return this;
    else if (elems.size() == 1) return this.addMember(elems.getFirst());
    else return this.addMember(elems.getFirst()).addMembers(elems.subList(1, elems.size()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Group group = (Group) o;
    return Objects.equals(new HashSet<>(members), new HashSet<>(group.members));
  }

  @Override
  public int hashCode() {
    return Objects.hash(members);
  }

  public Integer getMaxCapacity() {
    return maxCapacity;
  }

  public Integer getCurrentUsage() {
    return currentUsage;
  }

  public Integer getCurrentCapacity() {
    return getMaxCapacity() - getCurrentUsage();
  }

  @Override
  public String toString() {
    return "Group{"
        + "maxCapacity="
        + maxCapacity
        + ", members="
        + members
        + ", currentUsage="
        + currentUsage
        + ", attributeFrequency="
        + attributeFrequency
        + ", groupId="
        + groupId
        + '}';
  }
}
