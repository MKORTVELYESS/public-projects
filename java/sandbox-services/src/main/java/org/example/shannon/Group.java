package org.example.shannon;

import java.util.*;
import java.util.stream.IntStream;

public class Group {
    public Integer maxCapacity;
    public Element[] members;
    public Integer currentUsage;
    public Map<String, Integer> attributeFrequency;
    public UUID groupId;

    public Group(Integer maxCapacity) {
        this.currentUsage = 0;
        this.maxCapacity = maxCapacity;
        this.members = new Element[maxCapacity];
        this.groupId = UUID.randomUUID();
        this.attributeFrequency = new HashMap<>();
    }

    Boolean isFull() {
        return Objects.equals(currentUsage, maxCapacity);
    }

    private Boolean isEmpty() {
        return currentUsage == 0;
    }


    private Integer indexOfFirstNull() {
        for (int i = 0; i < members.length; i++) {
            if (members[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private Integer getCurrentAttributeFrequency(String attribute) {
        return attributeFrequency.getOrDefault(attribute, 0);
    }


    public Double shannonIndex() {

        if (currentUsage == 0) {
            return -1.0;
        }

        var weightedGeomean = attributeFrequency.values().stream()
                .map((freq) -> {
                    var p = (double) freq / currentUsage;
                    return Math.pow(p, p);
                }).reduce(1.0, (accumulator, next) -> accumulator * next);

        return Math.log((1 / weightedGeomean));
    }

    public Integer addMember(Element e) throws IndexOutOfBoundsException {
        if (!isFull()) {
            var indexToPopulate = indexOfFirstNull();
            members[indexToPopulate] = e;
            currentUsage++;
            attributeFrequency.put(e.getAttrib(), getCurrentAttributeFrequency(e.getAttrib()) + 1);
            return indexToPopulate;
        } else {
            throw new IndexOutOfBoundsException("Can not add more members as this group is at capacity");
        }
    }

    public void removeMember(Integer idx) throws IndexOutOfBoundsException {
        if (!(members[idx] == null)) {
            var attrib = members[idx].getAttrib();
            members[idx] = null;
            currentUsage--;
            attributeFrequency.put(attrib, getCurrentAttributeFrequency(attrib) - 1);
        } else {
            throw new IndexOutOfBoundsException("Can not remove, this index is already null");
        }
    }

    @Override
    public String toString() {
        return "Group{" +
                "maxCapacity=" + maxCapacity +
                ", members=" + Arrays.toString(members) +
                ", currentUsage=" + currentUsage +
                ", attributeFrequency=" + attributeFrequency +
                ", groupId=" + groupId +
                '}';
    }
}
