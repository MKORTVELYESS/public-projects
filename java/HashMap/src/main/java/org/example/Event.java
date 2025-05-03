package org.example;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Event {
    private static final StringBuffer buffer = new StringBuffer();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    private ZonedDateTime eventTime;
    private EventType eventType;
    private Integer productId;
    private Long categoryId;
    private CategoryCode categoryCode;
    private Float price;
    private Integer userId;


    public Event(ZonedDateTime eventTime, EventType eventType, Integer productId, Long categoryId, CategoryCode categoryCode, Float price, Integer userId) {
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.productId = productId;
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.price = price;
        this.userId = userId;
    }

    public Event(String eventCsv) {
        String[] parts = eventCsv.split(",");

        // Optional: validate size
        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid CSV format: " + eventCsv);
        }

        this.eventTime = parts[0].isEmpty() ? null :
                ZonedDateTime.parse(parts[0], formatter);
        setEventType(parts[1]);
        this.productId = parseInt(parts[2]);
        this.categoryId = parseLong(parts[3]);
        setCategoryCode(parts[4]);
        this.price = parseFloat(parts[6]);
        this.userId = parseInt(parts[7]);
    }


    private Integer parseInt(String s) {
        return s == null || s.isEmpty() ? null : Integer.valueOf(s);
    }

    private Long parseLong(String s) {
        return s == null || s.isEmpty() ? null : Long.valueOf(s);
    }

    private Float parseFloat(String s) {
        return s == null || s.isEmpty() ? null : Float.valueOf(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventTime, event.eventTime) && Objects.equals(eventType, event.eventType) && Objects.equals(userId, event.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTime, eventType, userId);
    }

    public ZonedDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(ZonedDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = EventType.valueOf(eventType.toUpperCase());
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryCode getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode.isEmpty() ? null : CategoryCode.valueOf(categoryCode.toUpperCase().replace(".", "_"));
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return new StringBuffer(255)
                .append("Event{")
                .append("eventTime=").append(eventTime)
                .append(", eventType='").append(eventType).append("'")
                .append(", productId=").append(productId)
                .append(", categoryId=").append(categoryId)
                .append(", categoryCode='").append(categoryCode).append("'")
                .append(", price=").append(price)
                .append(", userId=").append(userId).append("'}").toString();
    }


}
